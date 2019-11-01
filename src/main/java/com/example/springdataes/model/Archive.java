package com.example.springdataes.model;

import com.example.springdataes.annotation.DefaultValue;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

/**
 * @author liuzj
 */
@Data
@Document(indexName = "bigdata-archive")
public class Archive {

    @Id
    private String aid;

    @Field(type = FieldType.Text,fielddata = true)
    private String name;
    
    @Field(type = FieldType.Text)
    private String cid;
    
    @Field(type = FieldType.Text)
    private String residence_addr;
    
    @Field(type = FieldType.Text)
    private String phone_number;
    
    @Field(type = FieldType.Text)
    private String curr_address;
    
    @Field(type = FieldType.Text)
    private String gender;
    
    @Field(type = FieldType.Long)
    private Long age;
    
    @Field(type = FieldType.Text)
    private String phone_mac;
    
    @Field(type = FieldType.Text)
    private String transportation_card;
    
    @Field(type = FieldType.Text)
    private String transportation;
    
    @Field(type = FieldType.Long)
    private Long birthday;
    
    @Field(type = FieldType.Long)
    @DefaultValue
    private Long recent_snap_time;
    
    @Field(type = FieldType.Long)
    private Long person_file_create_time;
    
    @Field(type = FieldType.Long)
    @DefaultValue
    private Long image_count;
    
    @Field(type = FieldType.Text)
    private String avatar_url;
    
    @Field(type = FieldType.Text)
    private String big_image_url;
    
    @Field(type = FieldType.Text)
    private String small_image_url;
    
    @Field(type = FieldType.Text)
    private String target_rect;
    
    @Field(type = FieldType.Text)
    private String algo_version;
    
    @Field(type = FieldType.Long)
    private Long modify_time;
    
    @Field(type = FieldType.Long)
    @DefaultValue
    private Long is_deleted;

}

