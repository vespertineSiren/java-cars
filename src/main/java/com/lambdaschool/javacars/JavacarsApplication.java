package com.lambdaschool.javacars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavacarsApplication {

    public static final String EXCHANGE_NAME = "Car";
    public static final String QUEUE_NAME = "Log";
    public static void main(String[] args) {
        SpringApplication.run(JavacarsApplication.class, args);
    }

}

