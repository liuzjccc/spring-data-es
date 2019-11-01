package com.example.springdataes.dto;

import lombok.Data;

import java.util.List;

/**
 * 档案中心搜索
 *
 * @author liuzhijian
 * @date 2019-03-14
 */
@Data
public class ArchiveListFilterDTO implements java.io.Serializable {
    
    /**
     * 身份证号
     */
    private String personCid;
    
    /**
     * 姓名
     */
    private String personName;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 时间类型
     */
    private String timeType;
    
    /**
     * 档案类型（0: 全部档案（默认）1：实名档案 2：未实名档案 3：我的关注）
     */
    private Integer personFileType = 0;
    
    /**
     * 档案ID集合
     */
    private List<String> personFileIds;
    
    /**
     * 排序字段（0：创建时间；1：更新时间；2：汇集图片数量）
     */
    private Integer sortName;
    
    private Integer page;
    
    private Integer pageSize;
    
}
