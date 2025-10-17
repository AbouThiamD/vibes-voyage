package com.descodeuses.voyage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {
  "com.descodeuses.voyage",
  "com.descodeuses"         
})
public class VoyageApplication {
  public static void main(String[] args) {
    SpringApplication.run(VoyageApplication.class, args);
  }
}
