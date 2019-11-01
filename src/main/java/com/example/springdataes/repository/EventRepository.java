package com.example.springdataes.repository;

import com.example.springdataes.model.Event;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EventRepository extends ElasticsearchRepository<Event, String> {
    
    /**
     * 根据档案ID查找事件
     *
     * @param aid 档案ID
     * @return List
     */
    List<Event> findAllByAid(String aid);
}
