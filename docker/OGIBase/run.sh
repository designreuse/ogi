#!/bin/bash

#Create data structure

mkdir -p /data/tmp
mkdir -p /data/storage
mkdir -p /data/logs

#Run mysql
/app/bin/run-mysql.sh

# Run tomcat
#/app/tomcat/bin/startup.sh
/app/tomcat/bin/catalina.sh run
