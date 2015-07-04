# OGI
Image dans laquelle est installé le logiciel OGI.

## Build
    docker build -t jerep6/ogi:4.5.0 .
    
## Run

	docker run -ti -p 3306:3306 -p 8080:8080 -e "OGI_KEY=password for properties" -v /media/sf_jpinsolle/travail/acimflo/docker-data/mysql:/app/mysql -v /media/sf_jpinsolle/travail/acimflo/docker-data/storage:/app/ogi/storage jerep6/ogi
    

