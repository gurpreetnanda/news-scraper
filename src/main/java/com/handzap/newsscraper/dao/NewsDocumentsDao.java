package com.handzap.newsscraper.dao;

import com.handzap.newsscraper.model.NewsDocument;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsDocumentsDao {

  private final List<NewsDocument> newsDocumentList;

  @Autowired
  public NewsDocumentsDao() {
    newsDocumentList = new ArrayList<>();
  }

  public void insertDocument(NewsDocument newsDocument) {
    newsDocumentList.add(newsDocument);
  }

  public List<NewsDocument> getAll() {
    return new ArrayList<>(newsDocumentList);
  }

}
