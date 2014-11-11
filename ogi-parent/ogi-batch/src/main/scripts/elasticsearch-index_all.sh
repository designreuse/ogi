#!/bin/bash

SCRIPT_DIR=`dirname $0`
JAVA="/home/jerep6/install/jdk1.8.0/bin/java"

$JAVA -DpropertyConfigurerPassword="PROPERTIES_PWD" -Dspring.profiles.active=batch -Dlog4j.configurationFile=batch/log4j2-batchs.xml -cp "$SCRIPT_DIR/../../resources:$SCRIPT_DIR/../ogi-batchs.jar" org.springframework.batch.core.launch.support.CommandLineJobRunner META-INF/spring/batch/job-ElasticsearchIndexAll.xml jobElasticsearch-index_all -next

