#!/bin/bash

#Run mysql
mysqld_safe &

# Run tomcat
/app/tomcat/bin/startup.sh
