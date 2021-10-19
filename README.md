# spring-cloud-alibaba

1. Developemnt environment

   - Java 11
   - Maven 1.8.0
   - Docker 20.10.7
   - Docker Compose: 1.29.2

2. Installment

   - Execute the command `sh install-all.sh` under the directory `/project-initial/src/main/script`

3. Check the following services:

   | Service  | Address                      |
   | -------- | ---------------------------- |
   | Nacos    | http://localhost:8848/nacos  |
   | Sentinel | http://localhost:8090/       |
   | Zipkin   | http://localhost:9411/zipkin |
   | RabbitMQ | http://localhost:15672/      |

   

4. Launch all microservices

- gateway-service [http://localhost:8888/]

- authority-service [http://localhost:10000/authority]

- talent-service [http://localhost:9001/job]

- company-service [http://localhost:9002/company]

- job-service [http://localhost:9003/job]

- admin-service [http://localhost:8000/admin]

  

5. Test 

- Test Login

  Access POST http://localhost:8888/authority/api/v3/account/login with the request body below:

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

  - Access GET http://localhost:8888/job/api/v3/test with Authorization in Request Header, like:

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

  - Access POST http://localhost:8888/job/api/v3/save-success with Authorization in Request Header

    To check these 3 database tables job, company and candidate. Succeed if those 3 tables got one new record respectively.

  - Access POST http://localhost:8888/job/api/v3/save-rollback with Authorization in Request Header

    Succeed if those 3 tables have no new record inserted.

  



