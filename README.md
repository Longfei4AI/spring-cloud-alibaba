# spring-cloud-alibaba

1. Install Nacos

   - Mac with M1: 

     ```
     docker run --name nacos -e MODE=standalone -p 8848:8848 -p 9848:9848 -p 9849:9849 -d zill057/nacos-server-apple-silicon:2.0.3

   - Mac with Intel: 

     ```
     docker run --name nacos -e MODE=standalone -p 8848:8848 -d nacos/nacos-server:2.0.2
     ```

   - Visit dashboard: http://localhost:8848/

   - Add initial config file

     - Open http://localhost:8848/

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

   - Visit dashboard: http://localhost:9411/

3. Install Sentinel Dashboard

   - Download jar

     https://github.com/alibaba/Sentinel/releases/download/1.8.2/sentinel-dashboard-1.8.2.jar

   - Install

     ```
     java -jar sentinel-dashboard-1.8.2.jar
     ```

   - Visit dashboard: http://localhost:8080/

4. Launch all microservices

   - candidate-service
   - company-service
   - job-service
   - gateway 
   - sentinel-service
   - config

5. Test http://localhost/job/test

   succeed if the following returned:

   ```
   {"id":1,"name":"Tom","age":30}
   ```

   

