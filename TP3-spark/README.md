# TP3-spark: Spark Sales Analysis

## Description
This project implements Apache Spark applications to analyze sales data from a text file. It demonstrates distributed data processing using Spark RDDs (Resilient Distributed Datasets).

## Project Structure
```
TP3-spark/
├── src/main/java/ma/enset/
│   ├── APP1.java          # Exercise 1: Total sales by city
│   └── APP2.java          # Exercise 2: Sales by city and year
├── data/
│   └── ventes.txt         # Sample sales data
├── docker-compose.yml     # Spark cluster configuration
└── pom.xml               # Maven dependencies
```

## Exercises

### Exercise 1: Total Sales by City (APP1.java)
Calculates the total sales amount for each city from the sales data.

**Output Example:**
```
=== Total Sales by City ===
Lyon: 2150.0 €
Marseille: 1720.0 €
Paris: 4700.0 €
```

### Exercise 2: Sales by City and Year (APP2.java)
Groups sales data by both city and year to show trends over time.

**Output Example:**
```
=== Total Sales by City and Year ===
Lyon (2023): 1300.0 €
Lyon (2024): 850.0 €
Marseille (2023): 1200.0 €
Marseille (2024): 520.0 €
Paris (2023): 2500.0 €
Paris (2024): 2200.0 €
```

## Prerequisites
- Java 21
- Apache Maven
- Docker (for cluster deployment)

## Data Format
The `ventes.txt` file contains sales data in the format:
```
date ville produit prix
2023-01-15 Paris Laptop 1200
2023-01-16 Lyon Phone 800
```

## How to Run

### Local Mode (Development)
```bash
# Compile the project
mvn clean compile

# Run Exercise 1
mvn exec:java -Dexec.mainClass="ma.enset.APP1"

# Run Exercise 2
mvn exec:java -Dexec.mainClass="ma.enset.APP2"
```

### Cluster Mode (Production)
```bash
# Start Spark cluster
docker-compose up -d

# Build JAR file
mvn clean package

# Submit to cluster
docker exec -it spark-master spark-submit \
  --class ma.enset.APP1 \
  --master spark://spark-master:7077 \
  /path/to/TP3-spark-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Technologies Used
- **Apache Spark 3.5.5** - Distributed computing framework
- **Java 21** - Programming language
- **Maven** - Build and dependency management
- **Docker** - Containerization for cluster deployment

## Key Concepts Demonstrated
- Spark RDD operations (map, filter, reduce)
- Distributed data processing
- Pair RDD transformations
- Local vs Cluster execution modes