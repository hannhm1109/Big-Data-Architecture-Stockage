package com.example.weather.model;

public class WeatherReading {
    private final String station;
    private final double temperature;
    private final double humidity;

    public WeatherReading(String station, double temperature, double humidity) {
        this.station = station;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getStation() { return station; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }

    public WeatherReading withTemperatureInFahrenheit() {
        double fahrenheit = (temperature * 9.0 / 5.0) + 32.0;
        return new WeatherReading(station, fahrenheit, humidity);
    }

    @Override
    public String toString() {
        return String.format("WeatherReading{station='%s', temperature=%.1f, humidity=%.1f}",
                station, temperature, humidity);
    }
}