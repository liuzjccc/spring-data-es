package com.example.springdataes.repository;

import com.example.springdataes.model.Archive;
import com.example.springdataes.model.Builder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BuilderRepository extends ElasticsearchRepository<Builder, Long> {
    
}