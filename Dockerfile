# # Use a base image with Java and Tomcat pre-installed
# FROM tomcat:10.1.18

# # Set the working directory to the Tomcat webapps directory
# WORKDIR $CATALINA_HOME/webapps/

# # Build the Spring Boot application
# RUN mvn clean package -DskipTests

# # Remove the default ROOT application
# RUN rm -rf ROOT

# # Copy the Spring Boot application .war file to the webapps directory
# COPY /target/retail-edge-app-1.0-SNAPSHOT-exec.war retail-edge-app.war

# # Expose the default Tomcat port
# EXPOSE 8080

# # Start Tomcat when the Docker container launches
# CMD ["catalina.sh", "run"]


# Use a Maven image for the build stage
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the project files
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# Build the project
RUN mvn clean package -DskipTests

# Use a Tomcat image for deployment
FROM tomcat:10.1.18
WORKDIR /usr/local/tomcat/webapps

# Copy the built WAR file
COPY --from=build /app/target/retail-edge-app-1.0-SNAPSHOT-exec.war ./retail-edge-app.war

# Expose the Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]