# TP1 : Manipulation du système de fichiers HDFS

Ce TP a pour objectif de se familiariser avec les commandes de base du système de fichiers HDFS (Hadoop Distributed File System).

## Environnement

Le projet utilise Docker pour créer un cluster Hadoop avec :
- 1 NameNode
- 5 DataNodes
- 1 ResourceManager
- 1 NodeManager

## Mise en place

1. Cloner le repository
2. Se placer dans le dossier du TP1
3. Lancer les conteneurs avec `docker-compose up -d`
4. Vérifier que les conteneurs sont démarrés avec `docker-compose ps`

## Interface Web

Accédez à l'interface web HDFS : http://localhost:9870

## Exercices réalisés

1. Création d'une arborescence de répertoires dans HDFS
2. Manipulation de fichiers (création, copie, renommage, suppression)
3. Transfert de fichiers entre système local et HDFS
4. Navigation et affichage récursif du contenu

## Captures d'écran

![interface](https://github.com/user-attachments/assets/d29d5777-9e6f-44aa-a408-f83ace7ac0f2)

![results](https://github.com/user-attachments/assets/71651b55-3348-4e20-a3f9-bc798bd7b562)

## Commandes importantes utilisées

```bash
# Création de répertoires
hdfs dfs -mkdir /BDDC
hdfs dfs -mkdir /BDDC/JAVA
hdfs dfs -mkdir /BDDC/CPP
hdfs dfs -mkdir /BDDC/JAVA/Cours
hdfs dfs -mkdir /BDDC/JAVA/TPs
hdfs dfs -mkdir /BDDC/CPP/Cours
hdfs dfs -mkdir /BDDC/CPP/TPs

# Création de fichiers locaux
echo "Contenu du cours CPP1" > CoursCPP1
echo "Contenu du cours CPP2" > CoursCPP2
echo "Contenu du cours CPP3" > CoursCPP3

# Transfert de fichiers vers HDFS
hdfs dfs -put CoursCPP1 /BDDC/CPP/Cours/
hdfs dfs -put CoursCPP2 /BDDC/CPP/Cours/
hdfs dfs -put CoursCPP3 /BDDC/CPP/Cours/

# Affichage du contenu de fichiers
hdfs dfs -cat /BDDC/CPP/Cours/CoursCPP1
hdfs dfs -cat /BDDC/CPP/Cours/CoursCPP2
hdfs dfs -cat /BDDC/CPP/Cours/CoursCPP3

# Copie de fichiers dans HDFS
hdfs dfs -cp /BDDC/CPP/Cours/CoursCPP1 /BDDC/JAVA/Cours/
hdfs dfs -cp /BDDC/CPP/Cours/CoursCPP2 /BDDC/JAVA/Cours/
hdfs dfs -cp /BDDC/CPP/Cours/CoursCPP3 /BDDC/JAVA/Cours/

# Suppression de fichiers
hdfs dfs -rm /BDDC/JAVA/Cours/CoursCPP3

# Renommage de fichiers
hdfs dfs -mv /BDDC/JAVA/Cours/CoursCPP1 /BDDC/JAVA/Cours/CoursJAVA1
hdfs dfs -mv /BDDC/JAVA/Cours/CoursCPP2 /BDDC/JAVA/Cours/CoursJAVA2

# Création de fichiers pour les TPs
mkdir -p TempFiles
echo "Contenu du TP1CPP" > TempFiles/TP1CPP
echo "Contenu du TP2CPP" > TempFiles/TP2CPP
echo "Contenu du TP1JAVA" > TempFiles/TP1JAVA
echo "Contenu du TP2JAVA" > TempFiles/TP2JAVA
echo "Contenu du TP3JAVA" > TempFiles/TP3JAVA

# Transfert de fichiers vers HDFS
hdfs dfs -put TempFiles/TP1CPP /BDDC/CPP/TPs/
hdfs dfs -put TempFiles/TP2CPP /BDDC/CPP/TPs/
hdfs dfs -put TempFiles/TP1JAVA /BDDC/JAVA/TPs/
hdfs dfs -put TempFiles/TP2JAVA /BDDC/JAVA/TPs/

# Affichage récursif du contenu
hdfs dfs -ls -R /BDDC

# Suppression d'un fichier
hdfs dfs -rm /BDDC/CPP/TPs/TP1CPP

# Suppression récursive d'un répertoire
hdfs dfs -rm -r /BDDC/JAVA

# Affichage de l'état final
hdfs dfs -ls -R /BDDC



