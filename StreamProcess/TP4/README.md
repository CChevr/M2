# RENDU

1. Dans un premier temps, vous devez produire les messages au format JSON avec l’API Producer
   de Kafka. Vous utiliserez le processeur JSON Jackson. Voici un exemple de message:
   {"nom":"Rodriguez","prenom":"Ottis","cip":3846200,"prix":2.56,"idPharma":151}
   Vous devez ensuite consommer les enregistrements depuis le topic alimenté par le Producer et
   affichez les informations à l’écran

Lancement depuis le fichier main, fonction q1().

A la suite du cours sur la mise en oeuvre, et en particulier de la partie sur le format Avro, vous
devez adapter les codes du producer et du consumer de la question 1 pour gérer les enregistrements
au format Avro.
Il vous sera nécessaire de créer un schéma Avro (extension avsc).
L’approche classique correspond à définir la sérialisation de la valeur d’un message avec
AvroSerialization. Néanmoins, cette approche implique de charger la plateforme Confluent (plus de
900Mo). Vous utilisez une autre approche, plus légère, basée sur la bibliothèque bijection proposée
par Twitter (https://github.com/twitter/bijection).
Pour le moment, on se limite à la bonne réception des messages produits.

Lancement depuis le fichier main, fonction q2().

3. Vous changez la configuration de Kafka. On veut maintenant avoir 3 brokers, 3 partitions et un
  replication factor de 3.

3.1 Modifier le producer pour qu’il produise sur les 3 partitions d’un nouveau topic
   
3.2 Modifier le consommateur précédent pour qu’il consomme depuis les 3 partitions, affiche les
   transactions d’achat de médicaments et produise dans un nouveau topic (on le nomme top2 dans la
   suite de l’énoncé mais vous pouvez le nommer comme vous voulez) dans l’objectif de faire
   l’analyse des ventes par médicament/
   
3.3 Vous devez créer un groupe de consommateur de 3 consommateurs qui consommera le
   messages de top2 dans l’objectif de compter le cumul des ventes par médicament. Vous afficherez le
   cumul du médicaments qui a été impliqués dans la transaction “streamée”

Lancement depuis le fichier main, fonction q3().
Souci : 
- fonctionnen une fois, lors du deuxième lancement le pool de trois consomateurs censés analyser les prescriptions
n'affichent rien. Pour les faire fonctionnner de nouveau il suffit de changer le nom du groupe.
