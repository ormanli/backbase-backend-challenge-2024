FROM maven:3-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . ./

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENV spring_profiles_active=docker

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
