package ma.enset;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;


public class IncidentsByService {
    public static void main(String[] args) {
        // Configure Spark
        SparkConf conf = new SparkConf()
                .setAppName("Incidents Count by Service")
                .setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Read CSV file
            JavaRDD<String> lines = sc.textFile("data/incidents.csv");

            // Skip header and parse data
            JavaRDD<String> dataLines = lines.filter(line -> !line.startsWith("Id"));

            // Extract service from each line and count incidents
            JavaPairRDD<String, Integer> serviceRDD = dataLines
                    .filter(line -> !line.trim().isEmpty())
                    .mapToPair(line -> {
                        String[] parts = line.split(",");
                        if (parts.length >= 5) {
                            String service = parts[3].trim(); // Service is 4th column (index 3)
                            return new Tuple2<>(service, 1);
                        }
                        return new Tuple2<>("Unknown", 1);
                    });

            // Count incidents by service using reduceByKey
            JavaPairRDD<String, Integer> incidentsByService = serviceRDD
                    .reduceByKey((count1, count2) -> count1 + count2);

            // Display results sorted by service name for consistent output
            System.out.println("=== Analyse des incidents par service ===");
            System.out.println("Nombre d'incidents par service :");
            System.out.println("=================================");

            incidentsByService
                    .sortByKey()
                    .collect()
                    .forEach(result ->
                            System.out.printf("%-15s: %2d incidents%n", result._1, result._2)
                    );

            // Additional statistics
            long totalIncidents = incidentsByService
                    .map(tuple -> tuple._2)
                    .reduce((a, b) -> a + b);

            System.out.println("=================================");
            System.out.println("Total incidents: " + totalIncidents);

        } catch (Exception e) {
            System.err.println("Error processing incidents data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}