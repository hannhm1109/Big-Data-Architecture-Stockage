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

![image](https://github.com/user-attachments/assets/c02e3949-c2bf-40b7-907d-74aa80a5a83a)

![results](https://github.com/user-attachments/assets/71651b55-3348-4e20-a3f9-bc798bd7b562)


## Commandes importantes utilisées 

```bash
# Création de répertoires
hdfs dfs -mkdir /BDDC
hdfs dfs -mkdir /BDDC/JAVA
hdfs dfs -mkdir /BDDC/CPP
...

# Manipulation de fichiers
hdfs dfs -put local_file hdfs_path
hdfs dfs -cat hdfs_file_path
hdfs dfs -cp source_path dest_path
hdfs dfs -rm file_path
hdfs dfs -rm -r dir_path
...
