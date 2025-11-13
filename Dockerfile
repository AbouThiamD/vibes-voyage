
FROM maven:3.9.5-eclipse-temurin-21 AS build


WORKDIR /app


COPY pom.xml .


RUN mvn dependency:go-offline -B


COPY src /app/src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=target/*.jar


COPY --from=build /app/${JAR_FILE} app.jar


ENV SPRING_PROFILES_ACTIVE=prod
ENV TZ=Europe/Paris


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]