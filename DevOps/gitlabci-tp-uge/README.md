# gitlabci-tp-uge
Prise en main de gitlab CI pour le M2.

## Rendu 

Rendre ce TP aujourd'hui à la fin de la séance. (pensez à commiter sur votre branch.)


## Prérequis

- Créer un compte sur gitlab : https://gitlab.com/

- M'envoyer son UsernameID pour que je l'ajoute au projet

- faire une branch avec votre "nom-prennm" à partir du master du projet sur lequel vous avez été ajouté (vous aller recevoir un mail)

- Toute la suite sera sur votre branche.

## Prendre en main GitLab

Faire le quick start de Gitlab pour créer vos premiers pipelines :

https://docs.gitlab.com/ee/ci/quick_start/

## Rajouter un job 

- Rajouter un job (qui sera lancé entre le stage test et prod sur action "manuel") dans votre pipeline qui fera juste un echo de votre nom-prenom.

## Rajouter une rules :

- Rajouter une règle qui lance le dépoy prod uniquement si le job précedent est passé.

## Rajouter un script :

- Rajouter un script qui print toutes vos variables systeme. à la racine du projet gitlab 
- Rajouter un job à la fin qui lancera directement ce script.

## Les Services :

Modifier votre gitlabci.yml pour mettre en place des services. comme ci dessous :

https://docs.gitlab.com/ee/ci/services/

Expliquez ce qui est fait, en quoi cela facilite en vie?


