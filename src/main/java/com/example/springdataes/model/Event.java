package com.example.springdataes.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * liuzj
 */
@Data
@Document(indexName = "bigdata-event")
public class Event {
    
    private String aid;
    
    @Id
    private String thumbnail_id;
    
    private String thumbnail_url;
    
    private String image_id;
    
    private String image_url;
    
    private String sys_code;
    
    private Integer algo_version;
    
    private String gender_info;
    
    private String age_info;
    
    private String hairstyle_info;
    
    private String hat_info;
    
    private String glasses_info;
    
    private String race_info;
    
    private String mask_info;
    
    private String skin_info;
    
    private String pose_info;
    
    private Float quality_info;
    
    private String target_rect;
    
    private String target_rect_float;
    
    private String land_mark_info;
    
    private Float feature_quality;
    
    private String source_id;
    
    private Integer source_type;
    
    private String site;
    
    private Long time;
    
    private Long dt;
    
    private Long create_time;
    
    private Long modify_time;
    
    private String column1;
    
    private String column2;
    
    private String column3;
    
}
