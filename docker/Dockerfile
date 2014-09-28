FROM debian:wheezy
MAINTAINER jeremy <jerep6@gmail.com>

# MAJ des paquets
RUN apt-get update

# Install packahes
RUN apt-get install --no-install-recommends -y wget curl ca-certificates

# Install mysql and configure it
RUN apt-get install --no-install-recommends -y mysql-server

# Install le JDK d'Oracle
COPY softs/jdk-8u20-linux-x64.tar.gz /tmp/jdk.tar.gz
RUN mkdir -p /app/java \
&& tar -zxf /tmp/jdk.tar.gz -C /app/java \
&& mv /app/java/$(ls /app/java/) /app/java/jdk \
&& rm /tmp/jdk.tar.gz 

ENV PATH $PATH:/app/java/jdk/bin
ENV JAVA_HOME /app/java/jdk/

#Install tomcat
COPY softs/apache-tomcat-8.0.12.tar.gz /tmp/tomcat.tar.gz
RUN tar -zxf /tmp/tomcat.tar.gz -C /app/ \
&& mv /app/apache-tomcat* /app/tomcat \
&& rm /tmp/tomcat.tar.gz



# Configure mysql : create user admin and listen on all addresses
RUN sed -i -e"s/^bind-address\s*=\s*127.0.0.1/bind-address = 0.0.0.0/" /etc/mysql/my.cnf \
&& mysqld_safe \
& sleep 10s \
&& mysql -e "GRANT ALL ON *.* TO admin@'%' IDENTIFIED BY 'admin' WITH GRANT OPTION; FLUSH PRIVILEGES"

# Create app user
RUN mysqld_safe \
& sleep 10s \
&& mysql -e "GRANT ALL ON ogi.* TO ogi@'%' IDENTIFIED BY 'ogi' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0; CREATE DATABASE IF NOT EXISTS ogi;"

# Configure tomcat :
RUN sed -i '/<\/Context>/i \
<Resource \
        name="jdbc/ogi"\n\
        auth="Container"\n\
        type="javax.sql.DataSource"\n\
        url="jdbc:mysql://localhost:3306/ogi"\n\
        username="ogi"\n\
        password="ogi"\n\
        driverClassName="com.mysql.jdbc.Driver"\n\
        maxActive="20"\n\
        maxIdle="1"\n\
        maxWait="10000"\n\
        removeAbandoned="true"\n\
        removeAbandonedTimeout="60"\n\
        logAbandoned="true"\n\
        validationQuery="SELECT 1"\n\
        testOnBorrow="true"\n\
        />\n\
' /app/tomcat/conf/context.xml \
&& echo '\ 
CATALINA_PID="$CATALINA_BASE/tomcat.pid"\n\
CATALINA_OPTS="-Xms256M -Xmx400M -DpropertyConfigurerPassword=\"OGI_KEY\""' > /app/tomcat/bin/setenv.sh \
&& sed -i '/^common.loader\=/ s/$/ , "\/app\/ogi\/resources\/"/' /app/tomcat/conf/catalina.properties


# Create ogi structure
RUN mkdir -p /app/bin && mkdir -p /app/ogi/logs/ && mkdir -p /app/ogi/storage && mkdir -p /app/ogi/tmp && echo "OK"


# Get deployment scripts
ADD ogi_replace /app/bin/
RUN wget https://raw.githubusercontent.com/jerep6/ogi/master/scripts/ogi_deploy


