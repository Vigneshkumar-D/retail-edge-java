package com.retailedge.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ServerPortLogger {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void logServerPort() {
        String port = environment.getProperty("server.port", "8080"); // Default to 8080 if not set
        System.out.println("Server is running on port: " + port);
    }
}
