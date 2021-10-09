FROM openjdk:11-oracle
WORKDIR /apn
ADD sentinel-dashboard-1.8.2.jar sentinel.jar
EXPOSE 8080

CMD ["java", "-jar", "sentinel.jar"]
