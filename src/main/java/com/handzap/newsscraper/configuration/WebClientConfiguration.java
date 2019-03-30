package com.handzap.newsscraper.configuration;

import com.gargoylesoftware.htmlunit.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WebClientConfiguration {

  @Bean
  public WebClient webClient() {
    try (WebClient client = new WebClient()) {
      client.getOptions().setCssEnabled(false);
      client.getOptions().setJavaScriptEnabled(false);
      return client;
    }
  }

}
