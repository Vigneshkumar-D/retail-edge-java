package com.retailedge;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

// @SpringBootApplication
// @EnableAutoConfiguration
// @EnableScheduling
// //@EnableJpaAuditing
// @EnableAsync
// public class RetailsEdgeApplication {

//     public static void main(String[] args) {
//         SpringApplication.run(RetailsEdgeApplication.class, args);
//         System.out.println("Application is Running....");
//     }
// }


@SpringBootApplication
@EnableScheduling
// @EnableJpaAuditing // Uncomment if auditing is required later
@EnableAsync
public class RetailsEdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailsEdgeApplication.class, args);
        System.out.println("Application is Running....");
    }
}


