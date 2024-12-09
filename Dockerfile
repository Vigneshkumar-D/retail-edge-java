# Use Maven for the build stage
FROM maven:3.8.5-openjdk-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Use a slim JDK image for the final stage
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/retail-edge-app-1.0-SNAPSHOT.jar retail-edge-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "retail-edge-app.jar"]
