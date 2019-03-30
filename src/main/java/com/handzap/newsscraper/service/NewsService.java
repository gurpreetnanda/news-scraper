package com.handzap.newsscraper.service;

import com.google.common.base.Strings;
import com.handzap.newsscraper.dao.NewsDocumentsDao;
import com.handzap.newsscraper.model.NewsDocument;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewsService {

  private final NewsDocumentsDao newsDocumentsDao;

  @Autowired
  public NewsService(NewsDocumentsDao newsDocumentsDao) {
    this.newsDocumentsDao = newsDocumentsDao;
  }

  public List<String> getAvailableAuthors() {
    return newsDocumentsDao.getAll().stream()
        .map(NewsDocument::getAuthor)
        .collect(Collectors.toList());
  }

  public List<NewsDocument> searchArticles(String author, String keyword) {
    return newsDocumentsDao.getAll().stream()
        .filter(a -> {
          if (Strings.isNullOrEmpty(author)) {
            return true;
          }
          return author.equals(a.getAuthor());
        })
        .collect(Collectors.toList());
  }

}
