1.docker network create jukebox-mysql

//since mysql is not available for the platform arm64/v8 (I'm using M1 Macbook Pro), we need to use MariaDb which has a support for the platform
2.docker container run --name mysqldb --network jukebox-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=jukebox -d arm64v8/mariadb

others can use the below mysql

2.docker container run --name mysqldb --network jukebox-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=jukebox -d mysql:8

//To see the container bash, run the following command
3. docker container exec -it <container-id> bash

//Use the same name of the mysql container in the jdbc url string in application.properties
4. spring.datasource.url=jdbc:mysql://mysqldb/jukebox?createDatabaseIfNotExist=true&autoReconnect=true&useSSL=false

//build the springboot application image using the command
5. docker image build -t jukebox:v1 .


//Now run the already built image(jukebox:v1 in our case) as a container in the already existing network jukebox-mysql
6. docker container run --network jukebox-mysql --name jukebox-jdbc-container -p 8080:8080 -d jukebox:v1

Some additional docker commands
1. List docker containers
   docker container ls
 1.1 Stop docker container
     docker container stop <container id>
 1.2 Remove docker container
     docker container rm <container id>
2. List docker network
   docker network ls
 2.1 Remove docker network
     docker network rm <network id>