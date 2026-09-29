FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM jetty:11.0.15-jre17
COPY --from=builder /app/target/shop-mvc.war /var/lib/jetty/webapps/root.war
EXPOSE 8080
