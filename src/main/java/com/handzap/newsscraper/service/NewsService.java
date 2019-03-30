package com.handzap.newsscraper.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
    return Lists.newArrayList(newsDocumentsDao.findAll()).stream()
        .map(NewsDocument::getAuthor)
        .filter(author -> !Strings.isNullOrEmpty(author))
        .collect(Collectors.toList());
  }

  //  todo: Fix logic
  public List<NewsDocument> searchArticles(String author, String topic, String description) {
    return newsDocumentsDao.findByAuthorAndTopicAndDescription(author, topic, description);
  }

}
