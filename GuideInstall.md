# Installation

L'application nécessite : 

  * Java 8
  * Un conteneur de servet (Tomcat)
  * Une base de données relationnelle (MySQL)
  * Un ordonnanceur (CRON)

Ce guide décrit l'installation sur une debian wheezy.

## JAVA

    wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" -c "http://download.oracle.com/otn-pub/java/jdk/8-b132/jdk-8-linux-i586.tar.gz"
	
	tar -zxvf jdk-8-linux-i586.tar.gz

## MYSQL

### Installation

    aptitude install mysql-server mysql-client

### Configuration

Création d'un utilisateur avec sa propre base de données.

    mysql -u root -p
    CREATE USER 'ogi'@'localhost' IDENTIFIED BY 'password_for_user';
    GRANT USAGE ON * . * TO 'ogi'@'192.168.1.7' IDENTIFIED BY 'ogi' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;
    CREATE DATABASE IF NOT EXISTS `ogi` ;
    GRANT ALL PRIVILEGES ON `ogi` . * TO 'ogi'@'localhost';

### Connexion à distance
Pour autoriser l'accès àà mysql depuis un autre poste, il faut créer un autre utilisateur ayant les mêmes droits mais n'étant pas limité à localhost

	-- To allow remote access
    CREATE USER 'ogi'@'%' IDENTIFIED BY 'ogi';
	GRANT USAGE ON * . * TO 'ogi'@'%' IDENTIFIED BY 'ogi' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0 ;
    GRANT ALL PRIVILEGES ON `ogi` . * TO 'ogi'@'%';

De plus, il indiquer à mysql d'écouter non plus sur l'ip locale, mais sur toutes les IPs. Fichier */etc/mysql/my.cnf*

	bind-address            = 0.0.0.0
	
## TOMCAT

### Installation

    wget -c "http://download.nextag.com/apache/tomcat/tomcat-7/v7.0.52/bin/apache-tomcat-7.0.52.tar.gz"
	tar -zxvf apache-tomcat-7.0.52.tar.gz

### Configuration

Ajouter la datasource dans le fichier conf/context.xml

    <Resource
            name="jdbc/ogi"
            auth="Container"
            type="javax.sql.DataSource"

            url="jdbc:mysql://localhost:3306/ogi"
            username="ogi"
            password="password_for_user"

            driverClassName="com.mysql.jdbc.Driver"

            maxActive="20"
            maxIdle="1"
            maxWait="10000"
            removeAbandoned="true"
            removeAbandonedTimeout="60"
            logAbandoned="true"

            validationQuery="SELECT 1"
            testOnBorrow="true"
            />
	
Créer le fichier /bin/setenv.sh

    JAVA_HOME=/home/jerep6/install/jdk1.8.0
    CATALINA_PID="$CATALINA_BASE/tomcat.pid"
	CATALINA_OPTS="-Xms256M -Xmx400M -DpropertyConfigurerPassword=\"secret key for encrypt properties\""

	
Pour ne pas exploder les wars . Fichier conf/server.xml

	<Host name="localhost"  appBase="webapps" unpackWARs="false" autoDeploy="true">

Modifier le fichier conf/catalina.properties pour ajouter le dossier contenant les ressources externes au classpath

	common.loader=${catalina.base}/lib,${catalina.base}/lib/*.jar,${catalina.home}/lib,${catalina.home}/lib/*.jar,/home/install/ogi/resources
	
# Deploiement
Le déploiement consiste à : 

  * copier les éléments de l'enveloppe dans les bons dossiers du système. 
  * remplacer les properties dépendantes de l'environnement
  
Le script ogi_deploy réalise les tâches décrites ci-dessous. Le remplacement des properties se configure dans le script **ogi_replace** qui est automatiquement exécuté par **ogi_deploy**

	ogi_deploy --deploy-ws --deploy-ihm --deploy-resources --app-version 0.0.8

# Sauvegarde
La sauvegarde consiste à :

  * dumper la base de données
  * archiver les documents uploadés sur le disque local
  * recopier les documents sur un autre poste via rsync
  
Le script ogi_backup réalise les tâches décrites ci-dessus. 

	ogi_backup
	
Une sauvegarde est prévue toutes les semaines

	crontab -e
	# Backup tous les jeudi à 1h00 du matin
	0       1       *       *       thu       /home/jerep6/bin/ogi_backup

