package com.example.weather.model;

public class StationAverage {
    private final String station;
    private final double avgTemperature;
    private final double avgHumidity;
    private final long count;

    public StationAverage(String station, double avgTemperature, double avgHumidity, long count) {
        this.station = station;
        this.avgTemperature = avgTemperature;
        this.avgHumidity = avgHumidity;
        this.count = count;
    }

    public static StationAverage create(String station) {
        return new StationAverage(station, 0.0, 0.0, 0);
    }

    public StationAverage add(double temperature, double humidity) {
        long newCount = count + 1;
        double newAvgTemp = ((avgTemperature * count) + temperature) / newCount;
        double newAvgHumidity = ((avgHumidity * count) + humidity) / newCount;
        return new StationAverage(station, newAvgTemp, newAvgHumidity, newCount);
    }

    public String getStation() { return station; }
    public double getAvgTemperature() { return avgTemperature; }
    public double getAvgHumidity() { return avgHumidity; }
    public long getCount() { return count; }

    @Override
    public String toString() {
        return String.format("StationAverage{station='%s', avgTemp=%.1f°F, avgHumidity=%.1f%%, count=%d}",
                station, avgTemperature, avgHumidity, count);
    }
}