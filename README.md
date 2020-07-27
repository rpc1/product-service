# product-service

## Stack:
 - gradle
 - hibernate
 - postgres
 - spring boot
 - JUnit, mockito
 - docker
 
## How to build:
  -  "./gradlew build" - builds jar file
  -  "docker build -f ./docker/Dockerfile.deploy -t product-service ." - builds docker image
  -  "docker-compose -f ./docker/docker-compose.yml  up" - starts service image and postgres

## How to connect
http://server-name:8080/{route}

    
    

    
     
    


    




