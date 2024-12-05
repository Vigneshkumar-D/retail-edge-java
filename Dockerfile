# Stage 1: Build the application



# Stage 2: Create the runtime image
FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN mvn clean package -DskipTests
COPY --from=build /app/target/retail-edge-app-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
