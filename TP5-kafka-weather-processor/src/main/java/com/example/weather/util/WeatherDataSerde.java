package com.example.weather.util;

import com.example.weather.model.StationAverage;
import com.example.weather.model.WeatherReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WeatherDataSerde {

    public static Serde<WeatherReading> weatherReadingSerde() {
        return new WeatherReadingSerde();
    }

    public static Serde<StationAverage> stationAverageSerde() {
        return new StationAverageSerde();
    }

    private static class WeatherReadingSerde implements Serde<WeatherReading> {
        @Override
        public Serializer<WeatherReading> serializer() {
            return new WeatherReadingSerializer();
        }

        @Override
        public Deserializer<WeatherReading> deserializer() {
            return new WeatherReadingDeserializer();
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public void close() {}
    }

    private static class StationAverageSerde implements Serde<StationAverage> {
        @Override
        public Serializer<StationAverage> serializer() {
            return new StationAverageSerializer();
        }

        @Override
        public Deserializer<StationAverage> deserializer() {
            return new StationAverageDeserializer();
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public void close() {}
    }

    private static class WeatherReadingSerializer implements Serializer<WeatherReading> {
        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public byte[] serialize(String topic, WeatherReading data) {
            if (data == null) return null;
            // Simple CSV format: station,temperature,humidity
            String csv = String.format("%s,%.2f,%.2f",
                    data.getStation(), data.getTemperature(), data.getHumidity());
            return csv.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public void close() {}
    }

    private static class WeatherReadingDeserializer implements Deserializer<WeatherReading> {
        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public WeatherReading deserialize(String topic, byte[] data) {
            if (data == null) return null;
            try {
                String csv = new String(data, StandardCharsets.UTF_8);
                String[] parts = csv.split(",");
                if (parts.length == 3) {
                    return new WeatherReading(
                            parts[0].trim(),
                            Double.parseDouble(parts[1].trim()),
                            Double.parseDouble(parts[2].trim())
                    );
                }
            } catch (Exception e) {
                // Return null for invalid data
            }
            return null;
        }

        @Override
        public void close() {}
    }

    private static class StationAverageSerializer implements Serializer<StationAverage> {
        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public byte[] serialize(String topic, StationAverage data) {
            if (data == null) return null;
            // Format: station,avgTemp,avgHumidity,count
            String csv = String.format("%s,%.2f,%.2f,%d",
                    data.getStation(), data.getAvgTemperature(),
                    data.getAvgHumidity(), data.getCount());
            return csv.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public void close() {}
    }

    private static class StationAverageDeserializer implements Deserializer<StationAverage> {
        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {}

        @Override
        public StationAverage deserialize(String topic, byte[] data) {
            if (data == null) return null;
            try {
                String csv = new String(data, StandardCharsets.UTF_8);
                String[] parts = csv.split(",");
                if (parts.length == 4) {
                    return new StationAverage(
                            parts[0].trim(),
                            Double.parseDouble(parts[1].trim()),
                            Double.parseDouble(parts[2].trim()),
                            Long.parseLong(parts[3].trim())
                    );
                }
            } catch (Exception e) {
                // Return null for invalid data
            }
            return null;
        }

        @Override
        public void close() {}
    }
}