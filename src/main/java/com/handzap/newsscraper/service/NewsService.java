package com.handzap.newsscraper.service;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewsService {

  public List<String> getAvailableAuthors() {
    return Collections.emptyList();
  }

  public List<String> searchArticles(String authorName, String keyword) {
    return ImmutableList.of("gurpreet", "test");
  }

}
