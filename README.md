# spring-cloud-alibaba

1. Install Nacos

   - Mac with M1: 

     ```shell
     docker run --name nacos -e MODE=standalone -p 8848:8848 -p 9848:9848 -p 9849:9849 -d zill057/nacos-server-apple-silicon:2.0.3

   - Others: 

     ```shell
     docker run --name nacos -e MODE=standalone -p 8848:8848 -p 9848:9848 -p 9849:9849  -d nacos/nacos-server:2.0.2
     ```

   - Visit dashboard: http://localhost:8848/nacos

     

2. Install Zipkin

   ```shell
   docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin
   ```

   - Visit dashboard: http://localhost:9411/zipkin

3. Install Sentinel Dashboard

   - Install

     - WIth jar

       Download jar: https://github.com/alibaba/Sentinel/releases/download/1.8.2/sentinel-dashboard-1.8.2.jar

       ```shell
       java -Dserver.port=8090 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.2.jar
       ```

     - With docker（not for Mac with M1）

       ```shell
       docker run -d -p 8090:8080 --name sentinel-dashboard minghealtomni/sentinel-dashboard-1.8.2
       ```

   - Visit dashboard: http://localhost:8090/#/dashboard/home

4. Create namespaces and add  yaml configurations:

   - Open http://localhost:8848/nacos

   - Click `Namespace`, create 3 namespaces (prod, staging, dev)

   - Click `ConfigManagement` --> `Configurations` --> `dev`

   - Add a new configuration for each microservice

     Data ID:  Refer to the files' name from [config->main->resource/**]

     Group: `DEFAULT_GROUP`

     Format: `YAML`

     Configuration Content: 

     ​		Copy the files' content from [config->main->resource/**]

   - Add the config for Seata

     - Create config file in dev namespace

       Data ID:  `apn_tx_group.properties`

       Group: `DEFAULT_GROUP`

       Format: `PROPERTIES`

       Configuration Content: 

         1.   Get config from https://github.com/seata/seata/blob/1.4.0/script/config-center/config.txt

         2.   Update the following config

              ```properties
              service.vgroupMapping.apn_tx_group=default
              store.mode=redis
              store.redis.host=127.0.0.1
              store.redis.port=6379
              ```

   - 

5. Create database and tables

   Username: root

   Password: 

   ```mysql
   create database apn_v3;
   ```

   

6. Install Seata

   - Set up db

     - Create tables

       Get client sql from https://github.com/seata/seata/blob/1.4.0/script/client/at/db/mysql.sql

   - Download Seata Or using docker

     - Download jar: https://github.com/seata/seata/releases/tag/v1.4.0

     - Using docker [It does not work using my Mac with M1 chip]

       ```dockerfile
       1. docker run -d --name seata-server -p 8091:8091 seataio/seata-server:1.4.0
       2. docker cp seata-server:/seata-server/resources /Users/mydir/seata/
       3. docker rm -f seata-server
       4. docker run -d --name seata-server -p 8091:8091 -v /Users/mydir/seata/resources:/seata-server/resources seataio/seata-server:1.4.0
       ```

       

   - Set up config

     - Modify `file.conf`

       ```properties
       store.mode = "redis"
       store.redis.host = "127.0.0.1"
       store.redis.port = "6379"
       ```

       

     - Modify `registry.conf`

       ```properties
       registry.type = "nacos"
       registry.nacos.group = "DEFAULT_GROUP"
       registry.nacos.namespace = "dev"
       registry.nacos.username = "nacos"
       registry.nacos.password = "nacos"
       
       config.type = "nacos"
       config.nacos.namespace = "dev"
       config.nacos.username = "nacos"
       config.nacos.password = "nacos"
       config.nacos.dataId = "apn_tx_group.properties"
       ```

   - Startup Seata [Only for downloaded Seata]

     ```shell
     sh seata-server.sh -p 8091 -h 127.0.0.1 -m redis
     ```

     

7. Install RabbitMQ

   ```dockerfile
   docker run -e RABBITMQ_DEFAULT_USER=apn -e RABBITMQ_DEFAULT_PASS=apn --hostname apn-mq --name mq -p 15672:15672 -p 5672:5672 -d rabbitmq:3-management
   ```

   http://localhost:15672/

8. Launch all microservices

   - gateway-service
   - authority-service
   - candidate-service
   - company-service
   - job-service

   

9. Test 

   - Test Login

     Access POST http://localhost/authority/api/v3/login with the request body below:

     ```json
     {
         "username": "longfei",
         "password": 123456
     }
     ```

     Response body:

     ```json
     {
         "id": 1,
         "username": "longfei",
         "firstName": "Longfei",
         "lastName": "Wang",
         "email": "longfei.wang@altomni.com",
         "activated": true,
         "langKey": "EN",
         "imageUrl": "",
         "credit": null,
         "phone": null,
         "tenantId": null,
         "divisionId": null,
         "credential": {
             "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6I...",
             "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6I...",
             "scope": "read write",
             "expires_in": 7199
         }
     }
     ```

     

   - Test API

     - Access GET http://localhost/job/api/v3/test with Authorization in Request Header, like:

       Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5c......

     - Succeed if the following returned:

       ```json
       {
           "id": 1,
           "name": "Longfei",
           "age": 30,
           "status": 1
       }
       ```

       

   - Test global transaction

     - Access POST http://localhost/job/api/v3/save-success with Authorization in Request Header

       To check these 3 database tables job, company and candidate. Succeed if those 3 tables got one new record respectively.

     - Access POST http://localhost/job/api/v3/save-rollback with Authorization in Request Header

       Succeed if those 3 tables have no new record inserted.

     

   

