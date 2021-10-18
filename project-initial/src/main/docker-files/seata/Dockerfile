# docker build -t seata:1.4.0 .

FROM openjdk:11-oracle
WORKDIR /apn
ADD code seata
ENV IP=192.168.0.4
EXPOSE 8091

CMD ["sh", "seata/bin/seata-server.sh", "-p", "8091", "-h", "${IP}", "-m", "redis"]
