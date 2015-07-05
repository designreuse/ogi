FROM jerep6/java8-mysql-tomcat
MAINTAINER jerep6

# Expose mysql and tomcat
EXPOSE 3306
EXPOSE 8080

# Create ogi structure
RUN mkdir -p /app/bin \ 
 && mkdir -p /app/ogi/delivery \
 && mkdir -p /data/tmp \
 && mkdir -p /data/ogi/storage \
 && mkdir -p /data/elasticsearch

# Configure mysql
ADD mysql/my.cnf /etc/mysql/my.cnf
ADD mysql/mysql_change_database_directory.sh /tmp/

# Configure tomcat
ADD tomcat/context.xml 			/app/tomcat/conf/context.xml
ADD tomcat/setenv.sh 			/app/tomcat/bin/setenv.sh
ADD tomcat/catalina.properties	/app/tomcat/conf/catalina.properties

# Configure elasticsearch
ADD elasticsearch				/app/ogi/elasticsearch

# Get deployment scripts
ADD ogi_replace	/app/bin/
ADD ogi_deploy	/app/bin/
ADD ogi_backup	/app/bin/
ADD run.sh	/app/bin/
RUN chmod +x 	/app/bin/*

#Add delivery envelope
ONBUILD ADD delivery/OGI /app/ogi/delivery/OGI
ONBUILD RUN /app/bin/ogi_deploy --deploy-ws --deploy-ihm --deploy-resources --deploy-batchs
ONBUILD CMD /app/bin/run.sh

#Bypass layering on data. Use volume container or volume to save data
#VOLUME ["/data/"]
