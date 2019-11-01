package com.example.springdataes.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springdataes.dto.PersonfileDTO;
import com.example.springdataes.dto.PersonfileSnapDTO;
import com.example.springdataes.model.Archive;
import com.example.springdataes.model.Event;
import com.example.springdataes.repository.EsTemplateRepository;
import com.example.springdataes.utils.ReflectUtil;
import com.example.springdataes.utils.ThreadPooUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class MessageListener {
    
    
    @Autowired
    private EsTemplateRepository<Archive> archiveEsTemplateRepository;
    
    @Autowired
    private EsTemplateRepository<Event> eventEsTemplateRepository;
    
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * 档案kafka监听接收
     *
     * @param records 消息
     */
    @KafkaListener(topics = "bigdata-output-person-file")
    public void archivelisten(List<ConsumerRecord> records) {
        ThreadPooUtils.threadPool.execute(new ArchiveHandlerWorker(records));
    }
    
    /**
     * 事件kafka监听接收
     *
     * @param records 消息
     */
    @KafkaListener(topics = "bigdata-output-face-event")
    public void eventlisten(List<ConsumerRecord> records) {
        ThreadPooUtils.threadPool.execute(new EventHandlerWorker(records));
    }
    
    private class ArchiveHandlerWorker implements Runnable {
        
        private List<ConsumerRecord> records;
        
        public ArchiveHandlerWorker(List<ConsumerRecord> records) {
            this.records = records;
        }
        
        
        @Override
        public void run() {
            try {
                List<Archive> archives = new ArrayList<>();
                if (records.isEmpty()) {
                    return;
                }
                for (ConsumerRecord record : records) {
                    Optional<?> kafkaMessage = Optional.ofNullable(record.value());
                    if (kafkaMessage.isPresent()) {
                        Object message = record.value();
                        JSONObject jsonParam = JSONObject.parseObject(Objects.requireNonNull(message).toString());
                        PersonfileDTO personfileDTO = JSON.parseObject(String.valueOf(jsonParam), PersonfileDTO.class);
                        Archive archive = new Archive();
                        ReflectUtil.copyProperties(personfileDTO, archive);
                        archive.setBirthday(personfileDTO.getBirthDate() != null ? personfileDTO.getBirthDate().getTime() : null);
                        archive.setPerson_file_create_time(personfileDTO.getPersonFileCreateTime() != null ? personfileDTO.getPersonFileCreateTime().getTime() : null);
                        archive.setModify_time(System.currentTimeMillis());
                        archives.add(archive);
                    }
                }
                
                if (!CollectionUtils.isEmpty(archives)) {
                    archiveEsTemplateRepository.upSert(archives);
                    System.out.println(Thread.currentThread().getName() + "-------当前时间：" + System.currentTimeMillis() + "--------------接收档案：" + archives.size());
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private class EventHandlerWorker implements Runnable {
        
        private List<ConsumerRecord> records;
        
        public EventHandlerWorker(List<ConsumerRecord> records) {
            this.records = records;
        }
        
        
        @Override
        public void run() {
            try {
                List<Event> events = new ArrayList<>();
                if (records.isEmpty()) {
                    return;
                }
                for (ConsumerRecord record : records) {
                    Optional<?> kafkaMessage = Optional.ofNullable(record.value());
                    if (kafkaMessage.isPresent()) {
                        Object message = record.value();
                        JSONObject jsonParam = JSONObject.parseObject(Objects.requireNonNull(message).toString());
                        PersonfileSnapDTO personfileSnapDTO = JSON.parseObject(String.valueOf(jsonParam), PersonfileSnapDTO.class);
                        Event event = new Event();
                        ReflectUtil.copyProperties(personfileSnapDTO, event);
                        event.setTime(personfileSnapDTO.getTime() != null ? personfileSnapDTO.getTime().getTime() : null);
                        event.setDt(personfileSnapDTO.getTime() != null ? personfileSnapDTO.getTime().getTime() : null);
                        event.setCreate_time(personfileSnapDTO.getCreateTime() != null ? simpleDateFormat.parse(DateFormatUtils.format(personfileSnapDTO.getCreateTime().getTime(),"yyyy-MM-dd")).getTime() : null);
                        event.setModify_time(System.currentTimeMillis());
                        events.add(event);
                    }
                }
                
                if (!CollectionUtils.isEmpty(events)) {
                    eventEsTemplateRepository.upSert(events);
                    System.out.println(Thread.currentThread().getName() + "-------当前时间：" + System.currentTimeMillis() + "--------------接收事件：" + events.size());
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
