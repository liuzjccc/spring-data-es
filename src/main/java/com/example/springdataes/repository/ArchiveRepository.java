package com.example.springdataes.repository;

import com.example.springdataes.model.Archive;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArchiveRepository extends ElasticsearchRepository<Archive, String> {
    
    Archive findByAid(String aid);
    
    List<Archive> findByAidIn(List<String> aids);
    
}