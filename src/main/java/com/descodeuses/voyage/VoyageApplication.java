package com.descodeuses.voyage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
  "com.descodeuses.voyage",
  "com.descodeuses"         // <-- ajoute ce package
})
public class VoyageApplication {
  public static void main(String[] args) {
    SpringApplication.run(VoyageApplication.class, args);
  }
}
