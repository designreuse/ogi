@echo off

set JAVA="C:\Program Files\Java\jdk1.7.0_21\bin\java.exe"

%JAVA% -cp "../resources;../batchs.jar" org.springframework.batch.core.launch.support.CommandLineJobRunner META-INF/spring/batch/job-PasserelleSeLoger.xml jobPasserelle-seloger.com -next

pause