package com.handzap.newsscraper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class NewsScraperApplication {

  public static void main(String[] args) {
    SpringApplication.run(NewsScraperApplication.class, args);
  }

}
