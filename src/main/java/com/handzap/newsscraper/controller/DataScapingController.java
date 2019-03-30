package com.handzap.newsscraper.controller;

import com.handzap.newsscraper.service.DataScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("data-scrape")
@RestController
public class DataScapingController {

  private final DataScrapingService dataScrapingService;

  @Autowired
  public DataScapingController(DataScrapingService dataScrapingService) {
    this.dataScrapingService = dataScrapingService;
  }

  @GetMapping("refresh-data")
  public String refreshData() {
    dataScrapingService.initializeDatabase();
    return "Data Scraping refresh is completed!";
  }

}