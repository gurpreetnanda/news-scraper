package com.handzap.newsscraper.controller;

import com.handzap.newsscraper.service.NewsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("news")
@RestController
public class NewsController {

  private final NewsService newsService;

  @Autowired
  public NewsController(NewsService newsService) {
    this.newsService = newsService;
  }

  @GetMapping("authors")
  public List<String> authors() {
    return newsService.getAvailableAuthors();
  }

  @GetMapping("search")
  public List<String> articles(String authorName, String keyword) {
    return newsService.searchArticles(authorName, keyword);
  }

}
