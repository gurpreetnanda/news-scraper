package com.gsn.newsscraper.controller;

import com.gsn.newsscraper.service.DataScrapingService;
import io.swagger.annotations.ApiOperation;
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

  /**
   * This controller refreshes the data in the scrap dump.
   *
   * @return 200 OK along with static message.
   */
  @ApiOperation("Delete and refresh all the data which is dumped in News index in ES.")
  @GetMapping("refresh-data")
  public String refreshData() {
    dataScrapingService.initializeDatabase();
    return "Data Scraping refresh is completed!";
  }

}
