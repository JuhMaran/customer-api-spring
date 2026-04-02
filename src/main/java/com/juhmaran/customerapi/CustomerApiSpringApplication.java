package com.juhmaran.customerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CustomerApiSpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerApiSpringApplication.class, args);
  }

}
