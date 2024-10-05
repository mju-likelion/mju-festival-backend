package org.mju_likelion.festival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class FestivalApplication {

  public static void main(String[] args) {
    SpringApplication.run(FestivalApplication.class, args);
  }
}
