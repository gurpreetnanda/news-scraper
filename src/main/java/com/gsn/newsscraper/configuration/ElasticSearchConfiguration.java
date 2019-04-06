package com.gsn.newsscraper.configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

@Slf4j
@Configuration
public class ElasticSearchConfiguration {

  @Order(Ordered.HIGHEST_PRECEDENCE)
  @Bean
  public EmbeddedElastic embeddedElastic() throws IOException, InterruptedException {
    return EmbeddedElastic.builder()
        .withElasticVersion("6.5.0")
        .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9345)
        .withSetting(PopularProperties.CLUSTER_NAME, "elasticsearch")
        .withStartTimeout(100, TimeUnit.SECONDS)
        .build().start();
  }

}
