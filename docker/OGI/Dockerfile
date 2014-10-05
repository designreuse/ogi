FROM jerep6/java8-mysql-tomcat
MAINTAINER jerep6

# Create MySQL app user
RUN mysqld_safe \
& sleep 10s \
&& mysql -e "GRANT ALL ON ogi.* TO ogi@'%' IDENTIFIED BY 'ogi' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0; CREATE DATABASE IF NOT EXISTS ogi;"

# Configure tomcat :
ADD tomcat/context.xml 		/app/tomcat/conf/context.xml
ADD tomcat/setenv.sh 		/app/tomcat/bin/setenv.sh
ADD tomcat/catalina.properties	/app/tomcat/conf/catalina.properties


# Create ogi structure
RUN mkdir -p /app/bin && mkdir -p /app/ogi/logs/ && mkdir -p /app/ogi/storage && mkdir -p /app/ogi/tmp && mkdir -p /app/ogi/delivery


# Get deployment scripts
ADD ogi_replace /app/bin/
ADD ogi_deploy /app/bin/
RUN chmod +x /app/bin/*

# Add delivery envelope
ADD OGI-0.4.5 /app/ogi/delivery/OGI-0.4.5
RUN /app/bin/ogi_deploy --deploy-ws --deploy-ihm --deploy-resources --app-version 0.4.5
