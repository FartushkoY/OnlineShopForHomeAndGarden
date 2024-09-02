# First stage: build the application
FROM maven:4.0.0-jdk-21 AS build
COPY . /app
WORKDIR /app
RUN mvn package

FROM openjdk:21-slim
ENV SPRING_PROFILES_ACTIVE=prod
COPY target/OnlineShopForHomeAndGarden1-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]

ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh