# OGI image de base
Image de base utilisée pour livrer une version du logiciel OGI. Cette image contient les logiciels nécessaires et configurés pour OGI.

## Build
    docker build -t jerep6/ogi-base .
    
### MySQL
Il est nécessaire d’initialiser le répertoire de mysql car celui-ci est monté via un volume :

	docker run -ti -v /mnt/stockage1/docker/diaporama/mysql:/app/mysql/ jerep6/ogi-base /bin/bash
	sh /tmp/mysql_change_database_directory.sh
	
Il faut maintenant créer l’utilisateur mysql pour le logiciel ogi :
	
	MYSQL_USER="ogi"
	MYSQL_PWD="ogi"
	mysql -uroot -e "GRANT ALL ON ${MYSQL_USER}.* TO ${MYSQL_USER}@'%' IDENTIFIED BY '$MYSQL_PWD' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0; CREATE DATABASE IF NOT EXISTS ${MYSQL_USER};"

	
Lister les utilisateurs

	select host, user, password from mysql.user;