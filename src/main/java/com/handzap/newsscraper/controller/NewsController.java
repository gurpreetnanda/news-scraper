package com.handzap.newsscraper.controller;

import com.handzap.newsscraper.model.NewsDocument;
import com.handzap.newsscraper.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("news")
@RestController
@Api("Controller for News Related Operations.")
public class NewsController {

  private final NewsService newsService;

  @Autowired
  public NewsController(NewsService newsService) {
    this.newsService = newsService;
  }

  /**
   * Get the list of all authors.
   *
   * @return sorted list of all distinct authors.
   */
  @ApiOperation("Get sorted list of all distinct authors.")
  @GetMapping("authors")
  public List<String> authors() {
    return newsService.getAvailableAuthors();
  }

  /**
   * Search the available documents.
   *
   * @param author      to be searched.
   * @param topic       to be searched.
   * @param description to be searched.
   * @return documents containing all the requested params.
   */
  @ApiOperation("Get news documents containing which contain all the fields being passed.")
  @GetMapping("search")
  public List<NewsDocument> search(String author, String topic, String description) {
    return newsService.searchArticles(author, topic, description);
  }

}
