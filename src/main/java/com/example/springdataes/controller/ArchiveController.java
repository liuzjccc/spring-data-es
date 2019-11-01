package com.example.springdataes.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springdataes.dto.ArchiveListFilterDTO;
import com.example.springdataes.model.Archive;
import com.example.springdataes.repository.ArchiveRepository;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Administrator
 */
@RequestMapping("archive")
@RestController
public class ArchiveController {

    @Autowired
    ArchiveRepository archiveRepository;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 新增
     *
     * @param archive 档案
     * @return ResponseEntity
     */
    @PostMapping(value = "save")
    public ResponseEntity<Archive> save(@RequestBody Archive archive) {
        if (archive == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Archive result = archiveRepository.save(archive);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * 查询档案
     *
     * @param archiveListFilterDTO 条件集合
     * @return
     */
    @PostMapping(value = "list")
    public ResponseEntity<Map<String,Object>> findByName(@RequestBody ArchiveListFilterDTO archiveListFilterDTO) throws ParseException {
        if (archiveListFilterDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        // 名字过滤
        if (Strings.isNotBlank(archiveListFilterDTO.getPersonName())) {
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("name", archiveListFilterDTO.getPersonName());
//            FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("name",archiveListFilterDTO.getPersonName());
            boolBuilder.filter(matchQuery);
        }
    
        // 身份证过滤
        if (Strings.isNotBlank(archiveListFilterDTO.getPersonCid())) {
            TermQueryBuilder termQuery = QueryBuilders.termQuery("cid",archiveListFilterDTO.getPersonCid());
            boolBuilder.filter(termQuery);
        }
        
        // 时间过滤
        if (Strings.isNotBlank(archiveListFilterDTO.getStartTime()) && Strings.isNotBlank(archiveListFilterDTO.getEndTime())) {
            RangeQueryBuilder dateQuery;
            switch (archiveListFilterDTO.getTimeType()){
                case "0":
                    dateQuery = QueryBuilders.rangeQuery("person_file_create_time")
                            .lte(dateFormat.parse(archiveListFilterDTO.getEndTime()).getTime())
                                .gte(dateFormat.parse(archiveListFilterDTO.getStartTime()).getTime());
                    break;
                case "1":
                    dateQuery = QueryBuilders.rangeQuery("recent_snap_time")
                            .lte(dateFormat.parse(archiveListFilterDTO.getEndTime()).getTime())
                            .gte(dateFormat.parse(archiveListFilterDTO.getStartTime()).getTime());
                    break;
                default:
                    dateQuery = QueryBuilders.rangeQuery("person_file_create_time")
                        .lte(dateFormat.parse(archiveListFilterDTO.getEndTime()).getTime())
                        .gte(dateFormat.parse(archiveListFilterDTO.getStartTime()).getTime());
                    break;
            }
            
            boolBuilder.filter(dateQuery);
        }
       
        // 档案类型
        if (archiveListFilterDTO.getPersonFileType() != null) {
            ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery("cid");
            switch (archiveListFilterDTO.getPersonFileType()){
                case 1:
                    boolBuilder.must(existsQuery);
                    break;
                case 2:
                    boolBuilder.mustNot(existsQuery);
                    break;
                default:
                    break;
            }
        }
    
        // 分页&排序
        Sort sort;
        switch (archiveListFilterDTO.getSortName()){
            case 0:
                sort = Sort.by(Sort.Direction.DESC,"person_file_create_time");
                break;
            case 1:
                sort = Sort.by(Sort.Direction.DESC,"modify_time");
                break;
            case 2:
                sort = Sort.by(Sort.Direction.DESC,"image_count");
                break;
            default:
                sort = Sort.by(Sort.Direction.DESC,"person_file_create_time");
                break;
        }
        
        PageRequest pageRequest = PageRequest.of(archiveListFilterDTO.getPage(),archiveListFilterDTO.getPageSize(),sort);
        Page<Archive> page = archiveRepository.search(boolBuilder, pageRequest);
        
        Map<String,Object> result = new HashMap<>(4);
    
    
        result.put("maxPage",page.getTotalPages());
//        Archive a = page.get().findFirst().get();
        result.put("data",page.get().collect(Collectors.toList()));
        result.put("total",page.getTotalElements());
       
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    /**
     * 根据档案ID查找
     *
     * @param aid 档案ID
     * @return ResponseEntity
     */
    @GetMapping("findByAid")
    public ResponseEntity<Archive> findByAid(String aid){
        if (Strings.isBlank(aid)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            Archive archive = archiveRepository.findByAid(aid);
            return new ResponseEntity<>(archive,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 根据档案ID集合查找
     *
     * @param params 档案ID
     * @return ResponseEntity
     */
    @PostMapping("findByAids")
    public ResponseEntity<List<Archive>> findByAids(@RequestBody JSONObject params){
        if (CollectionUtils.isEmpty(params.getJSONArray("aids"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            List<String> aids = params.getJSONArray("aids").toJavaList(String.class);
            List<Archive> archives = archiveRepository.findByAidIn(aids);
            return new ResponseEntity<>(archives,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

