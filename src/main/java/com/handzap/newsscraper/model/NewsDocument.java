package com.handzap.newsscraper.model;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDocument {
  private String topic;
  private String description;
  private String author;

  public static NewsDocument from(HtmlPage newsDocumentPage) {
    String topic = newsDocumentPage.getFirstByXPath("//h1[@class='title']/text()").toString();
    String author = newsDocumentPage.getFirstByXPath("//a[contains(@class, 'auth-nm')]/text()")
        .toString();
    return new NewsDocument(topic, "", author);
  }
}
