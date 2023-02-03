FROM maven:3-eclipse-temurin-17-alpine as build
WORKDIR /app
COPY pom.xml pom.xml
COPY src src
COPY conf conf
RUN mvn package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/minimalapi-1.0-SNAPSHOT.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
