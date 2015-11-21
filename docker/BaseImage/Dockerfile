FROM debian:wheezy
MAINTAINER jerep6

# MAJ des paquets
RUN apt-get update

# Install packages File is needed for file type detection with Files.probeContentType
RUN apt-get install --no-install-recommends -y wget curl ca-certificates less procps vim cron file

# Install mysql and configure it
RUN apt-get install --no-install-recommends -y mysql-server

# Install le JDK d'Oracle
#COPY soft/jdk-8u20-linux-x64.tar.gz /tmp/jdk.tar.gz
RUN wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" -c "http://download.oracle.com/otn-pub/java/jdk/8u45-b14/jdk-8u45-linux-x64.tar.gz" -O /tmp/jdk.tar.gz
RUN mkdir -p /app/java \
&& tar -zxf /tmp/jdk.tar.gz -C /app/java \
&& mv /app/java/jdk* /app/java/jdk \
&& rm /tmp/jdk.tar.gz 

ENV PATH $PATH:/app/java/jdk/bin
ENV JAVA_HOME /app/java/jdk/

#Install tomcat
RUN wget "http://www.us.apache.org/dist/tomcat/tomcat-8/v8.0.28/bin/apache-tomcat-8.0.28.tar.gz" -O /tmp/tomcat.tar.gz
RUN tar -zxf /tmp/tomcat.tar.gz -C /app/ \
&& mv /app/apache-tomcat* /app/tomcat \
&& rm /tmp/tomcat.tar.gz


# Configure mysql : create user admin and listen on all addresses
RUN sed -i -e"s/^bind-address\s*=\s*127.0.0.1/bind-address = 0.0.0.0/" /etc/mysql/my.cnf \
&& mysqld_safe \
& sleep 10s \
&& mysql -e "GRANT ALL ON *.* TO admin@'%' IDENTIFIED BY '' WITH GRANT OPTION; FLUSH PRIVILEGES"

# Paris Timezone
RUN echo "Europe/Paris" > /etc/timezone\
&& cp /usr/share/zoneinfo/Europe/Paris /etc/localtime

# Search history (Page up and down, up and down)
RUN echo '\ 
"\e[5~": history-search-backward\n\
"\e[6~": history-search-forward\n\
"\e[A": history-search-backward\n\
"\e[B": history-search-forward\n\
' >> /etc/inputrc