package ma.enset;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class APP2 {
    public static void main(String[] args) {
        // Configure Spark
        SparkConf conf = new SparkConf()
                .setAppName("Sales by City and Year")
                .setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Read the sales file
            JavaRDD<String> salesLines = sc.textFile("data/ventes.txt");

            // Parse each line and extract city, year, and price
            JavaPairRDD<String, Double> cityYearPriceRDD = salesLines
                    .filter(line -> !line.trim().isEmpty())
                    .mapToPair(line -> {
                        String[] parts = line.split(" ");
                        if (parts.length >= 4) {
                            String date = parts[0];
                            String city = parts[1];
                            double price = Double.parseDouble(parts[3]);

                            // Extract year from date (format: YYYY-MM-DD)
                            String year = date.split("-")[0];

                            // Create composite key: "City-Year"
                            String cityYear = city + "-" + year;

                            return new Tuple2<>(cityYear, price);
                        }
                        return new Tuple2<>("Unknown-0000", 0.0);
                    });

            // Sum prices by city and year
            JavaPairRDD<String, Double> totalSalesByCityYear = cityYearPriceRDD
                    .reduceByKey((price1, price2) -> price1 + price2);

            // Display results sorted by key
            System.out.println("=== Total Sales by City and Year ===");
            totalSalesByCityYear
                    .sortByKey()
                    .collect()
                    .forEach(result -> {
                        String[] parts = result._1.split("-");
                        String city = parts[0];
                        String year = parts[1];
                        System.out.println(city + " (" + year + "): " + result._2 + " €");
                    });

        } finally {
            sc.close();
        }
    }
}