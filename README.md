# spring-cloud-alibaba

1. Install Nacos

   - Mac with M1: 

     ```
     docker run --name nacos -e MODE=standalone -p 8848:8848 -p 9848:9848 -p 9849:9849 -d zill057/nacos-server-apple-silicon:2.0.3

   - Others: 

     ```
     docker run --name nacos -e MODE=standalone -p 8848:8848 -p 9848:9848 -p 9849:9849  -d nacos/nacos-server:2.0.2
     ```

   - Visit dashboard: http://localhost:8848/nacos

     

2. Install Zipkin

   ```
   docker run -d -p 9411:9411 --name zipkin openzipkin/zipkin
   ```

   - Visit dashboard: http://localhost:9411/zipkin

3. Install Sentinel Dashboard

   - Install

     - WIth jar

       Download jar: https://github.com/alibaba/Sentinel/releases/download/1.8.2/sentinel-dashboard-1.8.2.jar

       ```
       java -Dserver.port=8088 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.2.jar
       ```

     - With docker（not for Mac with M1）

       ```
       docker run -d -p 8088:8080 --name sentinel-dashboard minghealtomni/sentinel-dashboard-1.8.2
       ```

   - Visit dashboard: http://localhost:8088/#/dashboard/home

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

   

5. Create database 

   Username: root

   Password: 

   ```
   create database apn_v3;
   ```

   

6. Launch all microservices

   - gateway-service
   - authority-service
   - candidate-service
   - company-service
   - job-service

7. Test 

   - Test Login

     Access POST http://localhost/authority/api/v3/login with the request body below:

     ```
     {
         "username": "longfei",
         "password": 123456
     }
     ```

     Response body:

     ```
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

       ```
       {
           "id": 1,
           "name": "Longfei",
           "age": 30,
           "status": 1
       }
       ```

       

     

     

   

