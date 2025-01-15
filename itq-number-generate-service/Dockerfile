FROM maven:3.9.9-eclipse-temurin-23 AS builder

RUN useradd -ms /bin/bash my_user
RUN groupadd -g 999 docker && usermod -aG docker my_user
USER my_user

WORKDIR /ready_app
COPY pom.xml .
COPY src ./src
COPY .testcontainers.properties /home/my_user/.testcontainers.properties

RUN mvn clean package -DskipTests
#Убрать -DskipTest и добавить -Dtestcontainers.enabled=true в строке выше для включения TestContainers, которые при запуске из Docker Compose не работают :)

FROM openjdk:23-jdk-slim
WORKDIR /ready_app

COPY --from=builder /ready_app/target/*.jar ready_app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "ready_app.jar"]