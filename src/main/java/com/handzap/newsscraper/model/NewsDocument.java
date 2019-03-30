package com.handzap.newsscraper.model;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.handzap.newsscraper.util.DataExtractionUtil;
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
    String topic = DataExtractionUtil.getTopicNameFromNewsPage(newsDocumentPage);
    String description = DataExtractionUtil.getDescriptionFromNewsPage(newsDocumentPage);
    String author = DataExtractionUtil.getAuthorNameFromNewsPage(newsDocumentPage);
    return new NewsDocument(topic, description, author);
  }

}
