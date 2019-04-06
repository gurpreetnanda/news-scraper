package com.gsn.newsscraper.dao;

import com.gsn.newsscraper.model.NewsDocument;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDocumentsDao extends ElasticsearchRepository<NewsDocument, String> {

  List<NewsDocument> findByAuthorAndTopicAndDescription(String author,
                                                        String topic,
                                                        String description);

}
