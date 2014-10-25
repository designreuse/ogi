#!/bin/bash

VOLUME_HOME="/data/mysql"
CONF_FILE="/etc/mysql/conf.d/my.cnf"

if [[ ! -d "$VOLUME_HOME/mysql" ]]; then
    echo "=> An empty or uninitialized MySQL volume is detected in $VOLUME_HOME"
    echo "=> Installing MySQL ..."
    mysql_install_db > /dev/null 2>&1
    echo "=> Done!"  
    echo "=> Creating admin user ..."
    /app/bin/create_mysql_app_user.sh
else
    echo "=> Using an existing volume of MySQL"
fi

 mysqld_safe &
