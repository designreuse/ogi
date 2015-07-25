FROM jerep6/java8-mysql-tomcat
MAINTAINER jerep6

# Expose mysql and tomcat
EXPOSE 3306
EXPOSE 8080

###### Root configuration ######
# Create user in order to don't write with root user in volume data
# Assume that uid and guid of user on host is 1000
RUN groupadd -g1000 ogi \
&& useradd -g1000 -u1000 -m -s /bin/bash ogi

# Configure mysql
ADD mysql/my.cnf /etc/mysql/my.cnf

RUN mkdir -p /app/ \
 &&	chown ogi:ogi /app/
 
###### User configuration ######
USER ogi

# Create ogi structure
RUN mkdir -p /app/bin \ 
 && mkdir -p /app/ogi/delivery

# Configure mysql
ADD mysql/mysql_change_database_directory.sh /tmp/

# Configure tomcat
ADD tomcat/context.xml 			/app/tomcat/conf/context.xml
ADD tomcat/setenv.sh 			/app/tomcat/bin/setenv.sh
ADD tomcat/catalina.properties	/app/tomcat/conf/catalina.properties

# Configure elasticsearch (plugins)
ADD elasticsearch				/app/ogi/elasticsearch

# Get deployment scripts
ADD ogi_replace	/app/bin/
ADD ogi_deploy	/app/bin/
ADD ogi_backup	/app/bin/
ADD run.sh	/app/bin/

###### Root configuration ######
USER root
# Les fichiers ajoutés via la commande ADD appartiennent à root même si l'instruction user est précisé. Change donc le owner
RUN chown -R ogi:ogi /app/
RUN chmod +x /app/bin/*


#Add delivery envelope
ONBUILD ADD delivery/OGI /app/ogi/delivery/OGI
ONBUILD RUN /app/bin/ogi_deploy --deploy-ws --deploy-ihm --deploy-resources --deploy-batchs
ONBUILD RUN chown -R ogi:ogi /app/
ONBUILD CMD /app/bin/run.sh

#Bypass layering on data. Use volume container or volume to save data
#VOLUME ["/data/"]
