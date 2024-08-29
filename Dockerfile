FROM openjdk:21-slim
COPY target/OnlineShopForHomeAndGarden1-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# First stage: build the application
FROM maven:4.0.0-jdk-21 AS build
COPY . /app
WORKDIR /app
RUN mvn package -DskipTests

FROM openjdk:21-slim
ENV SPRING_PROFILES_ACTIVE=prod
COPY target/OnlineShopForHomeAndGarden1-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
