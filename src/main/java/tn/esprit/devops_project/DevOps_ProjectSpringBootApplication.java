package tn.esprit.devops_project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevOps_ProjectSpringBootApplication {

    // Initialize the Log4j2 Logger
    private static final Logger logger = LogManager.getLogger(DevOps_ProjectSpringBootApplication.class);

    public static void main(String[] args) {
        // Log an info message at application startup
        logger.info("Starting DevOps Project Spring Boot Application");

        // Dummy product info to log
        String title = "Product1";
        double price = 99.99;
        int quantity = 50;
        String category = "Electronics";

        // Log product info message between startup messages
        logger.info("Product Info: Title: {}, Price: {}, Quantity: {}, Category: {}", title, price, quantity, category);

        // Run the Spring Boot application
        SpringApplication.run(DevOps_ProjectSpringBootApplication.class, args);

        // Log another message when application has successfully started
        logger.info("DevOps Project Spring Boot Application started successfully");
    }
}
