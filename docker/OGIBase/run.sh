#!/bin/bash

#Run mysql
/etc/inid.d/mysql start

# Run tomcat
#/app/tomcat/bin/startup.sh
/app/tomcat/bin/catalina.sh run
