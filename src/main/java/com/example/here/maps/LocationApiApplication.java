package com.example.here.maps;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info (title = "Location API", version = "1.0.0"))
public class LocationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationApiApplication.class, args);
    }

}
