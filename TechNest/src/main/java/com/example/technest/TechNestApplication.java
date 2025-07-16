package com.example.technest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //this annotation @EnableJpaAuditing points to your AuditorAware implementation
public class TechNestApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(TechNestApplication.class, args);
    }

}
