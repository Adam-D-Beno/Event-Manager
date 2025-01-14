package org.das.event_manager;

import org.das.event_manager.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventManagerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(EventManagerApplication.class, args);
        LOGGER.info("http://localhost:8080/swagger-ui/index.html#/");
    }

}
