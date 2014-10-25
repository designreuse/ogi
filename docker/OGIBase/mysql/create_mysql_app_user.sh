#!/bin/bash

MYSQL_USER=ogi
MYSQL_PWD=ogi

/usr/bin/mysqld_safe > /dev/null 2>&1 &

RET=1
while [[ RET -ne 0 ]]; do
    echo "=> Waiting for confirmation of MySQL service startup"
    sleep 5
    mysql -uroot -e "status" > /dev/null 2>&1
    RET=$?
done

echo "=> Creating MySQL user ${MYSQL_USER} with ${MYSQL_PWD} password"

mysql -uroot -e "GRANT ALL ON ${MYSQL_USER}.* TO ${MYSQL_USER}@'%' IDENTIFIED BY '$MYSQL_PWD' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0; CREATE DATABASE IF NOT EXISTS ${MYSQL_USER};"


echo "=> Done!"

mysqladmin -uroot shutdown
