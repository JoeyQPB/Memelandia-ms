FROM openjdk:17-oracle

WORKDIR /app

COPY target/update-service-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties

ENTRYPOINT ["java","-jar","app.jar"]
