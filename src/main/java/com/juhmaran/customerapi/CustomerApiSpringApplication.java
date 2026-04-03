package com.juhmaran.customerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableCaching
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class CustomerApiSpringApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerApiSpringApplication.class, args);
  }

}
