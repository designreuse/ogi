# OGI
Image dans laquelle est installé le logiciel OGI.

## Build
    docker build -t jerep6/ogi:4.5.0 .
    
## Run
**Lancer le container en daemon**

	docker run -ti --name ogi-latest -p 3306:3306 -p 8080:8080 -p 9200:9200 -e "OGI_KEY=password for properties" -v /media/sf_jpinsolle/travail/acimflo/docker-data/ogi/:/data/ -v /media/sf_jpinsolle/travail/acimflo/docker-data/ogi/bash_history_root.txt:/root/.bash_history -v /media/sf_jpinsolle/travail/acimflo/docker-data/ogi/bash_history_ogi.txt:/home/ogi/.bash_history jerep6/ogi

Explications :
* Le dossier */data* du conteneur est voué à accueillir toutes les données à persister.
* Le fichier *bash_history.txt* est utilisé pour garder l'historique entre les différentes instances d'images au cours du temps.
* Ports :
	* 3306 : MySQL
	* 8080 : Application OGI
	* 9200 : Elasticsearch HTTP
* *OGI_KEY* : valeur de la passphrase servant à déchiffrer les mots de passes dans les properties 
* *--name ogi-latest* : nom du conteneur
	
**Exécuter un shell bash dans un container**

	docker exec -it ogi-latest /bin/bash	
    

