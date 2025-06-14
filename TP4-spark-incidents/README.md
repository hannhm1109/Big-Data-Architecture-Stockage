# TP4-spark-incidents: Industrial Incident Analysis

## Description
This project implements Apache Spark applications to analyze industrial incidents from CSV data. The system processes incident reports to provide insights for management decision-making.

## Project Structure
```
TP4-spark-incidents/
├── src/main/java/ma/enset/
│   ├── IncidentsByService.java    # Exercise 1: Count incidents by service
│   └── TopYearsIncidents.java     # Exercise 2: Find top 2 years with most incidents
├── data/
│   └── incidents.csv              # Industrial incident data
├── docker-compose.yml             # Spark cluster configuration
└── pom.xml                       # Maven dependencies
```

## Data Format
The CSV file contains incident data with the following structure:
```csv
Id,titre,description,service,date
1,Panne électrique,Coupure de courant dans l'atelier,Maintenance,2023-01-15
2,Défaut qualité,Produit non conforme,Production,2023-02-20
```

## Exercises

### Exercise 1: Incidents Count by Service (IncidentsByService.java)
Analyzes incident distribution across different company services.

**Output Example:**
```
=== Nombre d'incidents par service ===
IT: 4 incidents
Logistique: 3 incidents
Maintenance: 4 incidents
Production: 4 incidents
RH: 2 incidents
Sécurité: 3 incidents
```

### Exercise 2: Top Years Analysis (TopYearsIncidents.java)
Identifies the two years with the highest number of incidents for trend analysis.

**Output Example:**
```
=== Les deux années avec le plus d'incidents ===
1. Année 2023: 10 incidents
2. Année 2024: 10 incidents
```

## Prerequisites
- Java 21
- Apache Maven
- Docker (for cluster deployment)

## How to Run

### Local Mode (Development)
```bash
# Compile the project
mvn clean compile

# Run Exercise 1 - Incidents by Service
mvn exec:java -Dexec.mainClass="ma.enset.IncidentsByService"

# Run Exercise 2 - Top Years Analysis
mvn exec:java -Dexec.mainClass="ma.enset.TopYearsIncidents"
```

### Cluster Mode (Production)
```bash
# Start Spark cluster
docker-compose up -d

# Build JAR file
mvn clean package

# Submit Exercise 1 to cluster
docker exec -it spark-master spark-submit \
  --class ma.enset.IncidentsByService \
  --master spark://spark-master:7077 \
  /path/to/TP4-spark-incidents-1.0-SNAPSHOT-jar-with-dependencies.jar

# Submit Exercise 2 to cluster
docker exec -it spark-master spark-submit \
  --class ma.enset.TopYearsIncidents \
  --master spark://spark-master:7077 \
  /path/to/TP4-spark-incidents-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Technologies Used
- **Apache Spark 3.5.5** - Distributed computing framework
- **Java 21** - Programming language
- **Maven** - Build and dependency management
- **Docker** - Containerization for cluster deployment

## Key Features
- CSV data processing and parsing
- Header row filtering
- Date extraction and year analysis
- Service-based incident aggregation
- Top-N analysis with ranking
- Distributed processing capabilities

## Business Value
- **Service Performance Monitoring**: Identify which services have the most incidents
- **Trend Analysis**: Track incident patterns over time
- **Resource Allocation**: Make data-driven decisions for maintenance and training
- **Risk Management**: Proactive identification of problematic areas