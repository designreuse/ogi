#!/bin/bash

JAVA="/home/jerep6/install/jdk1.8.0/bin/java"

$JAVA -DpropertyConfigurerPassword="PROPERTIES_PWD" -Dspring.profiles.active=batch -cp "../resources:../batchs.jar"org.springframework.batch.core.launch.support.CommandLineJobRunner META-INF/spring/batch/job-PasserelleSeLoger.xml jobPasserelle-seloger.com -next

