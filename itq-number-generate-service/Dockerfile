FROM maven:3.8.6-openjdk-23-slim AS builder

WORKDIR /itq/numbers-service

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:23-jdk-slim
WORKDIR /itq/numbers-service

COPY --from=builder /itq/numbers-service/target/*.jar itq-numbers-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "itq-numbers-service.jar"]