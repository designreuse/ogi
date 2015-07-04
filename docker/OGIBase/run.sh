#!/bin/bash

#Run mysql
/etc/init.d/mysql start

# Run tomcat
#/app/tomcat/bin/startup.sh
/app/tomcat/bin/catalina.sh run
