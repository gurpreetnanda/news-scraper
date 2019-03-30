package com.handzap.newsscraper.util;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.base.Strings;
import java.util.List;
import java.util.stream.Collectors;

public class DataExtractionUtil {

  public static final String EMPTY_STRING = "";
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
    return htmlPage.<HtmlAnchor>getByXPath("//div[@class='archiveBorder']//a")
        .stream()
        .map(HtmlAnchor::getHrefAttribute)
        .filter(href -> !Strings.isNullOrEmpty(href))
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

  public static List<String> getTopicUrls(HtmlPage a) {
    return a.<HtmlAnchor>getByXPath(
        "//div[@class='tpaper-container']//section//ul//a[starts-with(@href, 'https:')]")
        .stream()
        .map(HtmlAnchor::getHrefAttribute)
        .filter(href -> !Strings.isNullOrEmpty(href))
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

  public static String getTopicNameFromNewsPage(HtmlPage newsPage) {
    Object titleObject = newsPage.getFirstByXPath("//h1[@class='title']/text()");
    return null == titleObject ? EMPTY_STRING : titleObject.toString();
  }

  public static String getAuthorNameFromNewsPage(HtmlPage newsPage) {
    Object authorNameObject = newsPage.getFirstByXPath(
        "//a[contains(@class, 'auth-nm')]/text()");
    return null == authorNameObject ? EMPTY_STRING : authorNameObject.toString();
  }

  public static String getDescriptionFromNewsPage(HtmlPage newsPage) {
    return EMPTY_STRING;
  }

  public static List<DomNode> getArchiveContainersFromMainPage(HtmlPage mainPage) {
    return mainPage.<DomNode>getByXPath("//div[@class='archiveContainer']")
        .stream()
        .limit(LIMIT)
        .collect(Collectors.toList());
  }

}
