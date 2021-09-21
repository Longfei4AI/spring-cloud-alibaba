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

   - Add initial config file

     - Open http://localhost:8848/nacos

     - Click `ConfigManagement` --> `Configurations`

     - Add new file

       Data ID: `nacos-config-dev.yaml`

       Group: `DEFAULT_GROUP`

       Format: `YAML`

       Configuration Content: 

       ```
       user:
         name: Lily
         age: 28
       ```

       

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

4. On Nacos' page, create 3 namespaces (prod, staging, dev) and add yaml config files to them [ConfigManagement->Configurations]. Your can find referrence files in our project. [config->main->resource/**]

5. Launch all microservices

   - gateway-service
   - authority-service
   - candidate-service
   - company-service
   - job-service

6. Test http://localhost/job/test

   succeed if the following returned:

   ```
   {"id":1,"name":"Tom","age":30}
   ```

   

