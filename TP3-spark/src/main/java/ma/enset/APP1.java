package ma.enset;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class APP1 {
    public static void main(String[] args) {
        // Configure Spark
        SparkConf conf = new SparkConf()
                .setAppName("Sales by City")
                .setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Read the sales file
            JavaRDD<String> salesLines = sc.textFile("data/ventes.txt");

            // Parse each line and extract city and price
            JavaPairRDD<String, Double> cityPriceRDD = salesLines
                    .filter(line -> !line.trim().isEmpty()) // Filter empty lines
                    .mapToPair(line -> {
                        String[] parts = line.split(" ");
                        if (parts.length >= 4) {
                            String city = parts[1];
                            double price = Double.parseDouble(parts[3]);
                            return new Tuple2<>(city, price);
                        }
                        return new Tuple2<>("Unknown", 0.0);
                    });

            // Sum prices by city
            JavaPairRDD<String, Double> totalSalesByCity = cityPriceRDD
                    .reduceByKey((price1, price2) -> price1 + price2);

            // Display results
            System.out.println("=== Total Sales by City ===");
            totalSalesByCity.collect().forEach(result ->
                    System.out.println(result._1 + ": " + result._2 + " €")
            );

        } finally {
            sc.close();
        }
    }
}