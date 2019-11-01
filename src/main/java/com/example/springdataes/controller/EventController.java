package com.example.springdataes.controller;

import com.example.springdataes.dto.PersonfileSnapDTO;
import com.example.springdataes.model.Event;
import com.example.springdataes.repository.EsTemplateRepository;
import com.example.springdataes.repository.EventRepository;
import com.example.springdataes.utils.ReflectUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuzj
 */
@RequestMapping("event")
@RestController
public class EventController {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EsTemplateRepository<Event> eventEsTemplateRepository;
    
    /**
     * 新增
     *
     * @param event 事件
     * @return ResponseEntity
     */
    @PostMapping(value = "save")
    public ResponseEntity<Event> save(@RequestBody Event event) {
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Event result = eventRepository.save(event);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * 根据档案ID查找事件
     *
     * @param aid 档案ID
     * @return ResponseEntity
     */
    @GetMapping(value = "findByAid")
    public ResponseEntity<List<Event>> findByAid(String aid) {
        if (Strings.isBlank(aid)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            List<Event> result = eventRepository.findAllByAid(aid);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("update")
    public ResponseEntity<Event> update(@RequestBody PersonfileSnapDTO personfileSnapDTO){
        if (Strings.isBlank(personfileSnapDTO.getThumbnailId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Event event = new Event();
        try {
            ReflectUtil.copyProperties(personfileSnapDTO,event);
            eventEsTemplateRepository.upSert(event);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
