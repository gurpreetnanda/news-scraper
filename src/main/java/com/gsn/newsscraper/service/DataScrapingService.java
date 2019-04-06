package com.gsn.newsscraper.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Strings;
import com.gsn.newsscraper.dao.NewsDocumentsDao;
import com.gsn.newsscraper.model.NewsDocument;
import com.gsn.newsscraper.util.DataExtractionUtil;
import io.reactivex.Flowable;
import io.reactivex.internal.schedulers.ComputationScheduler;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataScrapingService {

  private static final String ARCHIVE_URL = "https://www.thehindu.com/archive/";

  private final WebClient webClient;
  private final NewsDocumentsDao newsDocumentsDao;
  private final int limit;

  @Autowired
  public DataScrapingService(WebClient webClient,
                             NewsDocumentsDao newsDocumentsDao,
                             @Value("${data-scrape-limit:5}") int limit) {
    this.webClient = webClient;
    this.newsDocumentsDao = newsDocumentsDao;
    this.limit = limit;
  }

  /**
   * Clear and initialize the ES dump by refreshing the data from the source.
   *
   * <p>
   * Exception is thrown when deleteAll() is called if there is no index.
   * This happens since Embedded ES is being used currently which is in-memory and deletes indexes at termination.
   */
  public void initializeDatabase() {
    try {
      newsDocumentsDao.deleteAll();
      log.info("Deleted all old documents from News index in ES.");
    } catch (Exception e) {

      log.info("News Index not found in ES.");
    }
    log.info("Begin scraping news articles from : {}", ARCHIVE_URL);
    scrapeAndSaveNewsDocuments();
    log.info("Scraping news articles from : {} is successful.", ARCHIVE_URL);
  }

  /**
   * Get the HTML Pages from The Hindu website archive and convert into document format and insert into ES.
   */
  private void scrapeAndSaveNewsDocuments() {
    List<DomNode> archiveContainersFromMainPage = DataExtractionUtil.getArchiveContainersFromMainPage(
        getWebPage(ARCHIVE_URL).get(), limit);
    AtomicInteger counter = new AtomicInteger();
    Flowable.fromIterable(archiveContainersFromMainPage)
        .subscribeOn(new ComputationScheduler())
        .parallel(8)
        .map(domNode -> DataExtractionUtil.getCalendarMonthUrls(domNode, limit))
        .flatMap(Flowable::fromIterable)
        .map(this::getWebPage)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(htmlPage -> DataExtractionUtil.getCalendarDateUrls(htmlPage, limit))
        .flatMap(Flowable::fromIterable)
        .map(this::getWebPage)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map((htmlPage -> DataExtractionUtil.getTopicUrls(htmlPage, limit)))
        .flatMap(Flowable::fromIterable)
        .map(this::getWebPage)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(NewsDocument::from)
        .filter(a -> !(Strings.isNullOrEmpty(a.getAuthor())
            && Strings.isNullOrEmpty(a.getTopic())
            && Strings.isNullOrEmpty(a.getDescription())))
        .doOnNext(newsDocumentsDao::save)
        .doOnNext(a -> counter.incrementAndGet())
        .sequential()
        .doOnComplete(() -> log.info("Inserted {} documents into ES.", counter.get()))
        .blockingSubscribe();
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
