package ma.enset;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.List;

/**
 * Exercise 2: Find the top 2 years with most incidents
 * This application analyzes incident trends over time to identify peak years
 */
public class TopYearsIncidents {
    public static void main(String[] args) {
        // Configure Spark
        SparkConf conf = new SparkConf()
                .setAppName("Top 2 Years with Most Incidents")
                .setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Read CSV file
            JavaRDD<String> lines = sc.textFile("data/incidents.csv");

            // Skip header and parse data
            JavaRDD<String> dataLines = lines.filter(line -> !line.startsWith("Id"));

            // Extract year from each incident and create pairs for counting
            JavaPairRDD<String, Integer> yearRDD = dataLines
                    .filter(line -> !line.trim().isEmpty())
                    .mapToPair(line -> {
                        String[] parts = line.split(",");
                        if (parts.length >= 5) {
                            String date = parts[4].trim(); // Date is 5th column (index 4)
                            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) { // Validate date format
                                String year = date.split("-")[0]; // Extract year from YYYY-MM-DD
                                return new Tuple2<>(year, 1);
                            }
                        }
                        return new Tuple2<>("Unknown", 1);
                    });

            // Count incidents by year
            JavaPairRDD<String, Integer> incidentsByYear = yearRDD
                    .reduceByKey((count1, count2) -> count1 + count2);

            // Get all years sorted by incident count (descending) for analysis
            List<Tuple2<String, Integer>> allYears = incidentsByYear
                    .mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1)) // Swap to sort by count
                    .sortByKey(false) // Sort descending by count
                    .mapToPair(tuple -> new Tuple2<>(tuple._2, tuple._1)) // Swap back to (year, count)
                    .collect();

            // Display all years for context
            System.out.println("=== Analyse temporelle des incidents ===");
            System.out.println("Incidents par année (ordre décroissant) :");
            System.out.println("========================================");

            for (int i = 0; i < allYears.size(); i++) {
                Tuple2<String, Integer> result = allYears.get(i);
                String rank = (i < 2) ? ">>> " : "    ";
                System.out.printf("%s%d. Année %s: %d incidents%n",
                        rank, i + 1, result._1, result._2);
            }

            // Highlight top 2 years
            System.out.println("========================================");
            System.out.println("LES DEUX ANNÉES AVEC LE PLUS D'INCIDENTS :");

            for (int i = 0; i < Math.min(2, allYears.size()); i++) {
                Tuple2<String, Integer> result = allYears.get(i);
                System.out.printf("🔥 %d. Année %s: %d incidents%n",
                        i + 1, result._1, result._2);
            }

            // Calculate total for context
            int totalIncidents = allYears.stream()
                    .mapToInt(tuple -> tuple._2)
                    .sum();

            System.out.println("========================================");
            System.out.println("Total incidents sur toutes les années: " + totalIncidents);

        } catch (Exception e) {
            System.err.println("Error processing incidents data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}