package com.example.springdataes.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PersonfileSnapDTO implements Serializable {
    
    private static final long serialVersionUID = 1417717873126992374L;
    
    private String aid;
    
    private String thumbnailId;
    
    private String thumbnailUrl;
    
    private String imageId;
    
    private String imageUrl;
    
    private String sysCode;
    
    private Integer algoVersion;
    
    private String genderInfo;
    
    private String ageInfo;
    
    private String hairstyleInfo;
    
    private String hatInfo;
    
    private String glassesInfo;
    
    private String raceInfo;
    
    private String maskInfo;
    
    private String skinInfo;
    
    private String poseInfo;
    
    private Float qualityInfo;
    
    private String targetRect;
    
    private String targetRectFloat;
    
    private String landMarkInfo;
    
    private Float featureQuality;
    
    private String sourceId;
    
    private Integer sourceType;
    
    private String site;
    
    private Date time;
    
    private Date createTime;
    
    private String column1;
    
    private String column2;
    
    private String column3;
    
}
