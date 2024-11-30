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


# Use a valid Maven image
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the pom.xml and source code
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# Build the project
RUN mvn clean package -DskipTests

# Use the Tomcat runtime
FROM tomcat:10.1.18
WORKDIR /usr/local/tomcat/webapps

# Copy the built .war file to the webapps directory
COPY --from=build /app/target/retail-edge-app-1.0-SNAPSHOT-exec.war ./retail-edge-app.war

# Expose Tomcat's port
EXPOSE 8080

# Run Tomcat
CMD ["catalina.sh", "run"]
