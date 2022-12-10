# TP Kubernetes (K8S) M2 UGE 2021 - Module DevOps

## 1. Quelques notions.

Qu'est ce qu'on pod ?
Un pods est un environnement d'exécution d'un ou de plusieurs conteneurs Docker

Qu'est ce qu'un node ?
Un node est un noeud esclave. Ce sont des machines hébergeant les conteneurs docker.
Ils répondent aux ordres d'un master.

A quoi sert K8S ?
Kubernetes permet d'orchestrer la création, la mise à l'échelle et le management des conteneurs. Il peut multiplier ou diminuer le nombre de copies d'un conteneur en fonction des besoins. 


Le développeur s'occupe t'il toujours de l'infrastructure?
Kubernetes gère également l'infrastructure, le développeur ne s'en occupe donc pas.

Qu'est-ce qu'un KUBELET?
Le kublet recueille des informations sur le node, et les renvoie au master à des fins de monitoring.
Il indique ainsi les ressources utilisées, les ressources disponibles, ou bien si le noeud ne fonctionne plus correctement.

Qu'est ce qu'un "Service" dans K8S?
Un service gère un ensemble de de conteneurs identique. Il à la charge d'en garantir la mise à l'échelle. Il possède une adresse IP et sert de point d'entrée pour communiquer avec les conteneurs qu'il gère.

Qu'est ce que EKS? et SWARM?
EKS pour Elastic Kubernetes Service est le service Kubernetes proposé par Amazon sur son cloud AWS.
Swarm est quant à lui l'orchestrateur maison de Docker. Il est ainsi en concurrence avec Kubernetes mais n'est pas imposé.

## 2. Cluster Minikube
Qu'est ce que Minikube?

Minikube est un kubernetes local. Il nous permet par exemple de nous entraîner à le manipuler et de tester des choses.

## 3. Un déploiement K8S
### Deployement
Qu'est ce qu'un déploiement Kubernetes ? 
Le déploiement est la phase où Kubernetes met à jours ses pods et ses replicaSets de l'état actuel à l'état désiré à un rythme contrôlé.
L'instruction de déploiement est déclarative.

En exécutant la commande:
`kubectl get deployments` 

le message suivant d'affiche:
`No resources found in default namespace.`

Après avoir lancé le déploiement en utilisant l'image Docker fourni, le message suivant d'affiche:
`deployment.apps/my-app-js created`

A la suite de cette commande, le déploiement à bien été effectué:
```
NAME        READY   UP-TO-DATE   AVAILABLE   AGE
my-app-js   1/1     1            1           118s
```

On retrouve également my-app-js dans les déploiements dans le dashboard.

### Pods
Pour voir l'ensemble des pods qui tournent : `kubectl get pods`

Le pod est t'il "RUNNING" ? S'il ne l'est pas => me le dire.
On remarque effectivement que le pod est en cours d'execution:
```
NAME                         READY   STATUS    RESTARTS   AGE
my-app-js-7ccfb7cfff-8njxg   1/1     Running   0          4m34s
```

Pour info : pour détruire un pod utiliser `kubectl delete -n default pod <PODS-ID>`

### Quelques commandes utile :

Voir la config de kubectl : `kubectl config view` 

Voir les events : `kubectl get events`. Avez vous des evenements? si oui, lequel?
On retrouve effectivement un liste d'évèenemnt. On y trouve par exemple le lancement du node, la création et le lancement du pod, ou bien encore la mise à l'echelle (scaling) du replicaSet

`kubectl cluster-info` que donne cette commande? expliquez en deux-trois mots
Non, cette commande permet de résumer les adresses des différents services.

## 4. Service K8S
Expliquez en quelques mots/lignes ce qu'est un Service dans K8S?
Dans k8s les services représentent le point d'entrée d'un lot de conteneurs dupliqués. Ainsi en communiquant avec le service via son adresse IP, il nous est possible de correspondre avec l'un des conteneur abritant le programme qui nous interesse.
Le lot de conteneurs est load balancé.


Nous allons créer un service à ce deployment. pour cela on utilise `expose deployment` dans notre cas :

`kubectl expose deployment my-app-js --type=LoadBalancer --port=8080`
`service/my-app-js exposed`

Sur Minikube, le type LoadBalancer rend le Service accessible via la commande minikube service

Expliquez ce qu'est un loadbalancer (LB).
Un loadBalancer permet de gérer la charge d'utilisation d'un conteneur. Si un conteneur est très utilisé et atteint la limite de ses ressources, le loadBalancer pourra alors dupliquer ce conteneur. Il peut également supprimer un conteneur en cas de faible affluence.


Aller voir si vous trouvez le service dans le dashboard et dans le terminal : `kubectl get services`
On le retrouve affectivement dans le dashboard dans la section : 
Service > Services 
Sur la ligne de "my-app-js" on retrouve bien le type "LoadBalancer"

De la même manière, dans le terminal on retrouve également la ligne:
```
NAME         TYPE
my-app-js    LoadBalancer
```

`minikube service my-app-js`

⚠️ ATTENTION parfois cela peut prendre un peu de temps.

Que se passe'il ? Quel est le message affiché?


```
|-----------|-----------|-------------|---------------------------|
| NAMESPACE |   NAME    | TARGET PORT |            URL            |
|-----------|-----------|-------------|---------------------------|
| default   | my-app-js |        8080 | http://192.168.49.2:31693 |
|-----------|-----------|-------------|---------------------------|
🏃  Tunnel de démarrage pour le service my-app-js.
|-----------|-----------|-------------|------------------------|
| NAMESPACE |   NAME    | TARGET PORT |          URL           |
|-----------|-----------|-------------|------------------------|
| default   | my-app-js |             | http://127.0.0.1:55255 |
|-----------|-----------|-------------|------------------------|
🎉  Ouverture du service default/my-app-js dans le navigateur par défaut...
```

L'exécution de la commande à ouvert une page web sur le service.
Cette commande permet de nous afficher l'url de connexion de nos services
On y retrouve le message de notre fichier contenu dans le docker:
`Bien joué ! Tu as fais ton premier DockerFile :)`

## 5. Les extensions

regarder un peu les extensions.
`minikube addons list`

Dans la vie pro vous utiliserez surement -: istio, logviewer, metrics-server

Pour ajouter un addon `minikube addons enable metrics-server`
| metrics-server              | minikube | enabled ✅   | Kubernetes                     |


pour l'afficher :

`kubectl get pod,svc -n kube-system`

Expliquer cette commande, pour la comprendre.
Cette commande nous permet faire un inventaire nos services, ainsi que des pods. Elle indique tout un lot d'informations utiles pour décrire ce qu'il se passe en local sur notre serveur.
On peut consulter "l'age" de nos services/pods, leur statut, le nombre de fois qu'ils ont été démarrés, le port sur lequel ils s'exécutent.

## 6. Scaling avec K8S

Lancer 5 exemplaire du pod précédent 

`kubectl scale -n default deployment my-app-js --replicas=5`

A quoi cela sert il (aller chercher sur internet si besoin.)
Cela permet de répartir la charge sur les 5 conteneurs.

`kubectl get pods`

Allez voir le dashboard pour comprendre et me dire si c'est cohérent avec le get pods ?
my-app-js-7ccfb7cfff-8njxg   1/1     Running   1 (22m ago)   21h
my-app-js-7ccfb7cfff-gnvrx   1/1     Running   0             57s
my-app-js-7ccfb7cfff-hd58k   1/1     Running   0             57s
my-app-js-7ccfb7cfff-nbqvl   1/1     Running   0             57s
my-app-js-7ccfb7cfff-wrj98   1/1     Running   0             57s

On retrouve bien le premier pods qui a été démarré en même temps que le serveur, ainsi que les 4 nouveaux démarrer suite à l'exécution de la commande.

## 7. Creer d’un fichier YAML 

`kubectl get deployment my-app-js -o yaml > my-app-js.yml`

Vous pourrez ainsi directement importer la conf via :  `kubectl create -f <file>`
 
 Expliquer en quelques lignes, quel est l'interet d'avoir ce fichier YAML
 Le fichier YAML permet de décrire l'environement Kubernetes, avec les différentes application contenues, le nombre de replicas, à quel moment les dupliquer., et encore d'atures informations.
 Il est donc possible de charger une configuration où on veut.

 ## 8. Acceder à un pod  et modifier le message.
 
`kubectl create deployment mynginxapp --image=nginx:latest`

`kubectl expose deployment mynginxapp --type=NodePort --port=80`

`minikube service mynginxapp --url`

Que voyez vous? Essayez de comprendre les 3 commandes.

Un nouvea service à été démarré et est accessible via une url donnée dans le terminal.
- Le première commande à créé un déploiement à partir d'une image nginx.
- La seconde commande permet d'exposer le déploiement comme un pod en lui affectant une adresse et un port
- la denrière commande permet de rendre accessible le service et de donner son url

Acceder à un pod (nginx) et modifier le fichier index.html.

Récupérer le nom du pods nginx(`kubectl get pods`)

Se connecter à un pod : `kubectl exec -ti mynginxapp-5d9b94449d-lw4jw bash`
`kubectl exec -ti mynginxapp-75d5dc9c57-wzrcz bash`


`cd /usr/share/nginx/html/`

`ls`

`rm index.html`

`echo "Je ne sais pas quoi ecrire, mterais je l'écris...." > index.html`

`exit`

`minikube service mynginxapp --url`

La page a bien été modifiée

## Cleanning 
Vous pouvez maintenant nettoyer les ressources que vous avez créées dans votre cluster :

`kubectl delete service my-app-js`

`kubectl delete deployment my-app-js`

Si nécessaire, arrêtez la machine virtuelle Minikube (VM) :

`minikube stop`

Si nécessaire, effacez la VM Minikube :

`minikube delete`

## Conclusion

Qu'avez vous pensez du TP ?
Ce TP à été pour moi une très bonne occasion de manipuler pour la première fois kubernetes et d'approfondir mes connaissances dessus.
J'ai beaucoup aimer faire un tour général des fonctionnalités offertes par cet outil, et de comprendre en quoi il est un atout pour la gestion d'applications sur des serveurs.

Comprennez vous mieux K8S ?
Oui

## Pour aller plus loin :
- Un ancien collègue d'Orange (Olivier Beyler) à mis en place un cours / Formation K8S très complète...
https://obeyler.github.io/Formation-K8S/

- Aurelie Vache sur Youtube pour comprendre simplement les concepts :
    Understanding Kubernetes in a visual way : https://www.youtube.com/c/AurelieVache/videos
