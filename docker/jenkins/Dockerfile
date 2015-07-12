FROM jenkins:1.609.1
MAINTAINER jerep6

USER root

# Install docker
RUN apt-get update && apt-get install -y apt-utils docker.io supervisor module-init-tools apparmor
VOLUME ["/var/lib/docker/"]

# Add jenkins to docker group in order to run docker
RUN usermod -G docker jenkins

##### Configure supervisord in order to run docker and jenkins
# Create directory for logs
RUN mkdir -p /var/log/supervisor \
	mkdir -p /var/log/docker \
	mkdir -p /var/log/jenkins
# Copy the supervisor.conf file
COPY supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Install the magic wrapper
ADD ./wrapdocker /usr/local/bin/wrapdocker
RUN chmod +x /usr/local/bin/wrapdocker

# Start supervisord when running the container
CMD /usr/bin/supervisord -c /etc/supervisor/conf.d/supervisord.conf

