package com.example.weather;

import com.example.weather.model.StationAverage;
import com.example.weather.model.WeatherReading;
import com.example.weather.util.WeatherDataSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

public class WeatherDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataProcessor.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "weather-data-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);

        StreamsBuilder builder = new StreamsBuilder();
        buildTopology(builder);

        Topology topology = builder.build();
        logger.info("Topology description:\n{}", topology.describe());

        KafkaStreams streams = new KafkaStreams(topology, props);

        // Add shutdown hook for graceful termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down weather data processor...");
            try {
                streams.close(Duration.ofSeconds(10));
            } catch (Exception e) {
                logger.error("Error during shutdown", e);
            }
        }));

        logger.info("Starting weather data processor application...");
        streams.start();

        // Keep the application running
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            logger.info("Application interrupted");
            streams.close();
            Thread.currentThread().interrupt();
        }
    }
    static void buildTopology(StreamsBuilder builder) {
        // Read from weather-data topic as CSV format: station,temperature,humidity
        KStream<String, String> weatherDataStream = builder.stream("weather-data");

        // Parse CSV and filter temperatures > 30°C
        KStream<String, WeatherReading> parsedWeatherStream = weatherDataStream
                .mapValues(WeatherDataProcessor::parseCSV)
                .filter((key, reading) -> reading != null)
                .filter((key, reading) -> reading.getTemperature() > 30.0);

        // Convert Celsius to Fahrenheit
        KStream<String, WeatherReading> fahrenheitStream = parsedWeatherStream
                .mapValues(WeatherReading::withTemperatureInFahrenheit);

        // Group by station and calculate averages
        KGroupedStream<String, WeatherReading> groupedByStation = fahrenheitStream
                .groupBy((key, reading) -> reading.getStation(),
                        Grouped.with(Serdes.String(), WeatherDataSerde.weatherReadingSerde()));

        // Aggregate to calculate running averages
        KTable<String, StationAverage> stationAverages = groupedByStation
                .aggregate(
                        () -> null,
                        (station, reading, aggregate) -> {
                            if (aggregate == null) {
                                return StationAverage.create(station);
                            }
                            return aggregate.add(reading.getTemperature(), reading.getHumidity());
                        },
                        Materialized.with(Serdes.String(), WeatherDataSerde.stationAverageSerde())
                );

        // Write results to station-averages topic
        stationAverages
                .toStream()
                .to("station-averages",
                        Produced.with(Serdes.String(), WeatherDataSerde.stationAverageSerde()));

        // Log the processing
        fahrenheitStream.foreach((key, reading) -> {
            logger.info("Processed high temperature reading: {}", reading);
        });

        stationAverages.toStream().foreach((station, average) -> {
            logger.info("Updated station average: {}", average);
        });
    }

    private static WeatherReading parseCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length != 3) {
                logger.warn("Invalid CSV format: {}", csvLine);
                return null;
            }

            String station = parts[0].trim();
            double temperature = Double.parseDouble(parts[1].trim());
            double humidity = Double.parseDouble(parts[2].trim());

            return new WeatherReading(station, temperature, humidity);
        } catch (Exception e) {
            logger.error("Error parsing CSV line: {}", csvLine, e);
            return null;
        }
    }
}