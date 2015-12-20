#!/bin/bash

# Dump env variable in file in order to run script via cron
echo '#!/bin/bash' > /app/bin/export_env.sh
env | sed -e 's/^/export /' -e 's/=\(.*\)/\="\1"/' >> /app/bin/export_env.sh
chmod +x /app/bin/export_env.sh

# Create ogi structure into data directory. Volume is mounted at run
mkdir -p /data/ && chown -R ogi:ogi /data/
su - ogi -c "mkdir -p /data/tmp && mkdir -p /data/ogi/storage && mkdir -p /data/elasticsearch && mkdir -p /data/backup && mkdir -p /data/mysql"

# Run mysql
/etc/init.d/mysql start

# Run cron
/etc/init.d/cron start

# Run tomcat
#/app/tomcat/bin/startup.sh
su - ogi -c "/app/tomcat/bin/catalina.sh run"
