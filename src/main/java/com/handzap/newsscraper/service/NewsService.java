package com.handzap.newsscraper.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.handzap.newsscraper.model.NewsDocument;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewsService {

  private static final int LIMIT = 2;
  private static final String ARCHIVE_URL = "https://www.thehindu.com/archive/";
  private final WebClient webClient;

  @Autowired
  public NewsService(WebClient webClient) {
    this.webClient = webClient;
  }

  public List<String> getAvailableAuthors() {
    List<String> pageAnchors = getArchiveContainersFromMainPage().stream()
        .map(this::getCalendarMonthUrls)
        .flatMap(Collection::stream)
        .map(this::getWebPage)
        .flatMap(Streams::stream)
        .map(this::getCalendarDateUrls)
        .flatMap(Collection::stream)
        .map(this::getWebPage)
        .flatMap(Streams::stream)
        .map(this::getTopicUrls)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    List<NewsDocument> collect = pageAnchors.stream()
        .map(this::getWebPage)
        .flatMap(Streams::stream)
        .map(NewsDocument::from)
        .collect(Collectors.toList());

    return new ArrayList<>(pageAnchors);
  }

  private List<DomNode> getArchiveContainersFromMainPage() {
    return getWebPage(ARCHIVE_URL).get().<DomNode>getByXPath("//div[@class='archiveContainer']")
        .stream()
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

  private List<String> getCalendarMonthUrls(DomNode domNode) {
    return domNode.<HtmlAnchor>getByXPath("div//a[starts-with(@href, 'https:')]").stream()
        .map(HtmlAnchor::getHrefAttribute)
        .filter(href -> !Strings.isNullOrEmpty(href))
        .limit(LIMIT)
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

  private List<String> getCalendarDateUrls(HtmlPage htmlPage) {
    return htmlPage.<HtmlAnchor>getByXPath("//div[@class='archiveBorder']//a")
        .stream()
        .map(HtmlAnchor::getHrefAttribute)
        .filter(href -> !Strings.isNullOrEmpty(href))
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

  private List<String> getTopicUrls(HtmlPage a) {
    return a.<HtmlAnchor>getByXPath(
        "//div[@class='tpaper-container']//section//ul//a[starts-with(@href, 'https:')]")
        .stream()
        .map(HtmlAnchor::getHrefAttribute)
        .filter(href -> !Strings.isNullOrEmpty(href))
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

  public List<String> searchArticles(String authorName, String keyword) {
    return ImmutableList.of("gurpreet", "test");
  }

}
