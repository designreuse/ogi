FROM jerep6/java8-mysql-tomcat
MAINTAINER jerep6

# Expose mysql and tomcat
EXPOSE 3306
EXPOSE 8080

# Create ogi structure
RUN mkdir -p /app/bin && mkdir -p /app/ogi/logs/ && mkdir -p /app/ogi/delivery

# Configure mysql
ADD mysql/my.cnf /etc/mysql/my.cnf
ADD mysql/create_mysql_app_user.sh /app/bin/
ADD mysql/run-mysql.sh /app/bin/

# Configure tomcat
ADD tomcat/context.xml 		/app/tomcat/conf/context.xml
ADD tomcat/setenv.sh 		/app/tomcat/bin/setenv.sh
ADD tomcat/catalina.properties	/app/tomcat/conf/catalina.properties


# Get deployment scripts
ADD ogi_replace /app/bin/
ADD ogi_deploy 	/app/bin/
ADD run.sh	/app/bin/
RUN chmod +x /app/bin/*

#Add delivery envelope
ONBUILD ADD OGI /app/ogi/delivery/OGI
ONBUILD RUN /app/bin/ogi_deploy --deploy-ws --deploy-ihm --deploy-resources
ONBUILD CMD /app/bin/run.sh

#Bypass layering on data. Use volume container or volume to save data
#VOLUME ["/data/"]
