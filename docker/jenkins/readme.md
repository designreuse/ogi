# Jenkins
Utilisation d'un jenkins custom afin d'installer docker. Se base sur l'image [docker officielle de Jenkins](https://registry.hub.docker.com/_/jenkins/).

## Initialisation
### SSH
Afin de récupérer le code et de releaser, il faut déposer la clé SSH spécifique au deploy dans le dossier .ssh du home de jenkins.

Il n'y a pas besoin de configurer de credential si une clé ssh portant le nom de id_rsa/id_dsa est présente dans le dossier ~/.ssh/ de l'utilisateur jenkins. Cette clé servira pour le clone/fetch et pour le push de maven release.
Dans tous les cas, les credentials de jenkins ne sont**pas** utilisés pour le push de maven release. Ils servent au clone/fetch.

### JAVA
JAVA_HOME = /usr/lib/jvm/java-8-openjdk-amd64/

### GIT
Afin d'utiliser le plugin maven release qui va effectuer des commits, il faut initialiser git avec les données de l'utilisateur.
Dans le volume HOME de jenkins créer le fichier *.gitconfig* contenant les instructions suivantes :

	[user]
	email = user@mail.com
	name = user name 

## Plugins
* [Git Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Git+Plugin)
* [Maven Release](https://wiki.jenkins-ci.org/display/JENKINS/M2+Release+Plugin)

## Run
	
	docker run -d --privileged --name jenkins -p 54000:8080 -v /mnt/stockage1/docker/jenkins/home:/var/jenkins_home/ -v /mnt/stockage1/docker/jenkins/docker:/var/lib/docker/ localhost:5000/jenkins
	
Explications :
* Le dossier */var/jenkins_home/* représente le home de jenkins. Il faut l'initialiser avec la clé SSH servant à releaser sur github (dossier .ssh)
* Le dossier */var/lib/docker/* contient les images buildées par docker
* Ports 8080 : port HTTP de jenkins
	


    

