package com.handzap.newsscraper.util;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataExtractionUtil {

  private static final String EMPTY_STRING = "";
  private static final int LIMIT = 2;

  private DataExtractionUtil() {
  }

  public static List<String> getCalendarMonthUrls(DomNode domNode) {
    return domNode.<HtmlAnchor>getByXPath("div//a[starts-with(@href, 'https:')]").stream()
        .map(HtmlAnchor::getHrefAttribute)
        .filter(href -> !Strings.isNullOrEmpty(href))
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

  public static List<String> getCalendarDateUrls(HtmlPage htmlPage) {
    return limitAndgetHrefUsingXPath(htmlPage, "//div[@class='archiveBorder']//a");
  }

  public static List<String> limitAndgetHrefUsingXPath(HtmlPage htmlPage, String xPath) {
    return htmlPage.<HtmlAnchor>getByXPath(xPath).stream()
        .map(HtmlAnchor::getHrefAttribute)
        .filter(href -> !Strings.isNullOrEmpty(href))
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

  public static List<String> getTopicUrls(HtmlPage htmlPage) {
    return limitAndgetHrefUsingXPath(htmlPage,
                                     "//div[@class='tpaper-container']//section//ul//a[starts-with(@href, 'https:')]");
  }

  public static String getTopicNameFromNewsPage(HtmlPage newsPage) {
    return getFirstByXpath(newsPage, "//h1[@class='title']/text()");
  }

  private static String getFirstByXpath(HtmlPage htmlPage, String xPath) {
    Object object = htmlPage.getFirstByXPath(xPath);
    return null == object ? EMPTY_STRING : object.toString();
  }

  public static String getAuthorNameFromNewsPage(HtmlPage newsPage) {
    return getFirstByXpath(newsPage, "//a[contains(@class, 'auth-nm')]/text()");
  }

  public static String getDescriptionFromNewsPage(HtmlPage newsPage) {
    String description = newsPage.<DomText>getByXPath(
        "//div[starts-with(@id,'content-body')]/descendant::*/text()").stream()
        .filter(Objects::nonNull)
        .map(DomText::toString)
        .collect(Collectors.joining());
    return null == description ? EMPTY_STRING : description;
  }

  public static List<DomNode> getArchiveContainersFromMainPage(HtmlPage mainPage) {
    return mainPage.<DomNode>getByXPath("//div[@class='archiveContainer']")
        .stream()
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

}
