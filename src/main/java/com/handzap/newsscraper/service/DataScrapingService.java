package com.handzap.newsscraper.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import com.handzap.newsscraper.dao.NewsDocumentsDao;
import com.handzap.newsscraper.model.NewsDocument;
import com.handzap.newsscraper.util.DataExtractionUtil;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataScrapingService {

  private static final String ARCHIVE_URL = "https://www.thehindu.com/archive/";

  private final WebClient webClient;
  private final NewsDocumentsDao newsDocumentsDao;

  @Autowired
  public DataScrapingService(WebClient webClient,
                             NewsDocumentsDao newsDocumentsDao) {
    this.webClient = webClient;
    this.newsDocumentsDao = newsDocumentsDao;
    this.initializeDatabase();
  }

  public void initializeDatabase() {
    log.info("Begin scraping news articles from : {}", ARCHIVE_URL);
    scrapeNewsDocuments()
        .forEach(newsDocumentsDao::insertDocument);
    log.info("Scraping news articles from : {} is successful.", ARCHIVE_URL);
  }

  public List<NewsDocument> scrapeNewsDocuments() {
    return DataExtractionUtil.getArchiveContainersFromMainPage(getWebPage(ARCHIVE_URL).get())
        .stream()
        .map(DataExtractionUtil::getCalendarMonthUrls)
        .flatMap(Collection::stream)
        .map(this::getWebPage)
        .flatMap(Streams::stream)
        .map(DataExtractionUtil::getCalendarDateUrls)
        .flatMap(Collection::stream)
        .map(this::getWebPage)
        .flatMap(Streams::stream)
        .map(DataExtractionUtil::getTopicUrls)
        .flatMap(Collection::stream)
        .map(this::getWebPage)
        .flatMap(Streams::stream)
        .map(NewsDocument::from)
        .collect(Collectors.toList());
  }

  private Optional<HtmlPage> getWebPage(String url) {
    if (Strings.isNullOrEmpty(url)) {
      return Optional.empty();
    }
    try {
      return Optional.of(webClient.getPage(url));
    } catch (Exception e) {
      log.error("Unable to fetch page data from: {}", url);
      throw new IllegalStateException();
    }
  }

}
