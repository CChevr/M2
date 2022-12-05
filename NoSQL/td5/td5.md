# TD5: MongoDB : Modeling – Administration – Java API

## B. We will import a CSV dataset into a new database

### 8. Import the movie information from the movies.csv file into the moviedb database. The first line

corresponds to the fields of the file. The information will be stored in the movies collection.
Import du fichier csv dans la collection films de la base mymoviedb
lancer l'executable d'import de mongo depuis le dossier mongodb database tool

```js
mongoimport -d mymoviedb -c films --type csv --file movies.csv --headerline
```

### 9. Create an admin user with read / write rights on the admin database and the ability to administer all databases. Restart the server with the authentication option, then create a user with read-only rights on the movies database. Relaunch the console and log in with this new user. Read and write.

Création de l'utilisateur avec les drotis admin en lecture/écriture.
```js
db.createUser({user:'admin',pwd:'pass', roles:[{role:'readWrite',db:'admin'},{role:'userAdminAnyDatabase',db:'admin'}]})
```

restart serveur avec l'option d'autehntification
```js
./mongod --dbpath /tmp --auth
```

A la connexion  de la base de données, toute lecture ou écriture nécessite une authentification.

### 10. What is happening if we restart mongod without the --auth option ?

sans l'option d'authentification lors du démarrage de la base,  aucun droit n'est requis ni aucune authentification.

## C. Using the movie database, we will write a Java program that will manipulate the data. Create a Maven project and insert the following dependance in the pom.xml file:

