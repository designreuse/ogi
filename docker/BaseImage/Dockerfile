FROM debian:wheezy
MAINTAINER jerep6

# MAJ des paquets
RUN apt-get update

# Install packages
RUN apt-get install --no-install-recommends -y wget curl ca-certificates less

# Install mysql and configure it
RUN apt-get install --no-install-recommends -y mysql-server

# Install le JDK d'Oracle
#COPY soft/jdk-8u20-linux-x64.tar.gz /tmp/jdk.tar.gz
RUN wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" -c "http://download.oracle.com/otn-pub/java/jdk/8u20-b26/jdk-8u20-linux-x64.tar.gz" -O /tmp/jdk.tar.gz
RUN mkdir -p /app/java \
&& tar -zxf /tmp/jdk.tar.gz -C /app/java \
&& mv /app/java/jdk* /app/java/jdk \
&& rm /tmp/jdk.tar.gz 

ENV PATH $PATH:/app/java/jdk/bin
ENV JAVA_HOME /app/java/jdk/

#Install tomcat
RUN wget "http://www.motorlogy.com/apache/tomcat/tomcat-8/v8.0.14/bin/apache-tomcat-8.0.14.tar.gz" -O /tmp/tomcat.tar.gz
RUN tar -zxf /tmp/tomcat.tar.gz -C /app/ \
&& mv /app/apache-tomcat* /app/tomcat \
&& rm /tmp/tomcat.tar.gz


# Configure mysql : create user admin and listen on all addresses
RUN sed -i -e"s/^bind-address\s*=\s*127.0.0.1/bind-address = 0.0.0.0/" /etc/mysql/my.cnf \
&& mysqld_safe \
& sleep 10s \
&& mysql -e "GRANT ALL ON *.* TO admin@'%' IDENTIFIED BY '' WITH GRANT OPTION; FLUSH PRIVILEGES"
