# Use a base image with Java and Tomcat pre-installed
FROM tomcat:10.1.18

# Set the working directory to the Tomcat webapps directory
WORKDIR $CATALINA_HOME/webapps/

# Remove the default ROOT application
RUN rm -rf ROOT

# Copy the Spring Boot application .war file to the webapps directory
COPY ./container/target/retail-edge-app-1.0-SNAPSHOT-exec.war retail-edge-app.war

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat when the Docker container launches
CMD ["catalina.sh", "run"]
