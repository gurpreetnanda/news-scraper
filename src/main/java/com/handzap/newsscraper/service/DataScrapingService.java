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
  }

  /**
   * Clear and initialize the ES dump by refreshing the data from the source.
   * <p>
   * Exception is thrown when deleteAll() is called if there is no index.
   * This happens since Embedded ES is being used currently which is in-memory and deletes indexes at termination.
   */
  //  todo: Use flowables for faster operations
  public void initializeDatabase() {
    try {
      newsDocumentsDao.deleteAll();
      log.info("Deleted all old documents from News index in ES.");
    } catch (Exception e) {

      log.info("News Index not found in ES.");
    }
    log.info("Begin scraping news articles from : {}", ARCHIVE_URL);
    scrapeNewsDocuments()
        .forEach(newsDocumentsDao::save);
    log.info("Scraping news articles from : {} is successful.", ARCHIVE_URL);
  }

  /**
   * Get the HTML Pages from The Hindu website archive and convert into document format to be inserted to ES.
   *
   * @return Collection of News Documents.
   */
  //  todo: Use flowables for faster operations
  private List<NewsDocument> scrapeNewsDocuments() {
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
