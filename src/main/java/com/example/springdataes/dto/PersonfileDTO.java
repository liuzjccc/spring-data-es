package com.example.springdataes.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

@Data
public class PersonfileDTO {
    
    /**
     * 档案ID
     */
    private String aid;
    
    /**
     * 姓名
     */
    @JSONField(name = "personName")
    private String name;
    
    /**
     * 身份证
     */
    @JSONField(name = "identityNo")
    private String cid;
    
    
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss", name = "createTime")
    private Date personFileCreateTime;
    
    /**
     * 户籍地
     */
    private String residencePlace;
    
    /**
     * 现居住地
     */
    private String currAddress;
    
    /**
     * 性别
     */
    private String gender;
    
    /**
     * 年龄
     */
    private Long age;
    
    /**
     * 生日
     */
    @JSONField(name = "birthday", format = "yyyy-MM-dd")
    private Date birthDate;
    
    /**
     * 头像地址
     */
    private String avatarUrl;
    
    /**
     * 手机号
     */
    @JSONField(name = "phoneNo")
    private String phoneNumber;
    
    /**
     * 汇集图片数
     */
    private Long imageCount;
    
    /**
     * 算法版本
     */
    private String algoVersion;
    
    /**
     * 大图地址
     */
    @JSONField(name = "imageUrl")
    private String bigImageUrl;
    
    /**
     * 小图地址
     */
    @JSONField(name = "thumbnailUrl")
    private String smallImageUrl;
    
    /**
     * 人脸框位置
     */
    private String targetRect;
    
    /**
     * 操作类型（update | insert）
     */
    private String action;
    
}
