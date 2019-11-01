package com.example.springdataes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringDataEsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SpringDataEsApplication.class, args);
    }
    
}
