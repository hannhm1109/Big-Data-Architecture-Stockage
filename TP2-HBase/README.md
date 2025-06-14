# TP2 : Manipulation d'une table HBase avec des commandes SHELL

Ce TP a pour objectif de se familiariser avec les commandes de base d'Apache HBase pour créer et manipuler des tables NoSQL.

## Environnement

Le projet utilise Docker pour créer un serveur HBase :
- 1 conteneur HBase incluant ZooKeeper

## Mise en place

1. Cloner le repository
2. Se placer dans le dossier du TP2
3. Lancer le conteneur avec `docker-compose up -d`
4. Se connecter au shell HBase avec `docker exec -it tp2-hbase_hbase_1 hbase shell`

## Interface Web

Accédez à l'interface web HBase : http://localhost:16010

## Exercices réalisés

1. Création d'une table avec deux familles de colonnes
2. Insertion de données pour différents utilisateurs
3. Lecture de données (get, scan)
4. Mise à jour et suppression de données
5. Manipulation avancée (scan avec plage de clés, comptage)

## Captures d'écran

![screen1](https://github.com/user-attachments/assets/a30b685e-d94e-4248-b2dd-20b93dc2bcac)
![screen2](https://github.com/user-attachments/assets/979cf76b-85e2-4eab-9652-d00da4a0d98a)

## Commandes importantes utilisées

```bash
# Création de la table
create 'users', 'info', 'contact'

# Insertion de données
put 'users', 'user1', 'info:prenom', 'ahmed'
put 'users', 'user1', 'info:nom', 'tazi'
put 'users', 'user1', 'contact:email', 'ahmed.tazi@example.com'
put 'users', 'user1', 'contact:phone', '0102030405'

# Lecture de données
get 'users', 'user1'
scan 'users'

# Mise à jour
put 'users', 'user2', 'contact:email', 'asmae.karimi@gmail.com'

# Suppression
delete 'users', 'user1', 'contact:phone'
deleteall 'users', 'user2'

# Scan avec plage
scan 'users', {STARTROW => 'user1', ENDROW => 'user3'}

# Comptage et description
count 'users'
describe 'users'
