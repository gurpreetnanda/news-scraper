package com.handzap.newsscraper.model;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.handzap.newsscraper.util.DataExtractionUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.UUIDs;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "news", type = "_doc")
public class NewsDocument {

  @Id
  private String id;
  private String topic;
  private String description;
  private String author;

  public static NewsDocument from(HtmlPage newsDocumentPage) {
    String topic = DataExtractionUtil.getTopicNameFromNewsPage(newsDocumentPage);
    String description = DataExtractionUtil.getDescriptionFromNewsPage(newsDocumentPage);
    String author = DataExtractionUtil.getAuthorNameFromNewsPage(newsDocumentPage);
    return new NewsDocument(UUIDs.randomBase64UUID(), topic, description, author);
  }

}
