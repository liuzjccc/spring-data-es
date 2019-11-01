package com.example.springdataes.utils;

import com.example.springdataes.annotation.DefaultValue;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzj
 */
public class ReflectUtil {
    
    /**
     * 获取ES Document注解
     *
     * @param clazz class
     * @return Document
     * @throws Exception
     */
    public static Document getDocument(Class clazz) throws Exception {
        Document annotation = (Document) clazz.getAnnotation(Document.class);
        if (annotation == null) {
            throw new Exception("Document annotation is not exist");
        }
        return annotation;
    }
    
    /**
     * obj to map
     *
     * @param obj 待转换对象
     * @return Map
     * @throws Exception
     */
    public static Map<String,Object> Obj2Map(Object obj,Boolean useDefaultValue) throws Exception{
        Map<String,Object> map=new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
            boolean accessible = field.isAccessible();
            try {
                
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
                Object value = field.get(obj);
                if (value != null) {
                    map.put(StringUtil.stringHumpConversion(field.getName()), value);
                } else if (useDefaultValue && defaultValue != null) {
                    map.put(StringUtil.stringHumpConversion(field.getName()), defaultValue.value());
                }
            } catch (Exception e) {
                throw e;
            } finally {
                field.setAccessible(accessible);
            }
        }
        return map;
    }
    
    /**
     * 获取字段值
     *
     * @param field 字段
     * @param obj 对象
     * @return Object
     */
    public static Object getFieldValue(Field field, Object obj) throws IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw e;
        } finally {
            field.setAccessible(isAccessible);
        }
    }
    
    /**
     * 对象互相拷贝
     *
     * @param source 源对象
     * @param target 目标对象
     * @throws IllegalAccessException 异常
     */
    public static void copyProperties(Object source, Object target) throws IllegalAccessException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
    
        Field[] sourceFields = source.getClass().getDeclaredFields();
    
        Field[] targetFields = target.getClass().getDeclaredFields();
        
        for (Field sourcefield: sourceFields) {
            for (Field targetField: targetFields) {
                
                if (targetField.getName().equals(StringUtil.stringHumpConversion(sourcefield.getName()))
                        && targetField.getType().getSimpleName().equals(sourcefield.getType().getSimpleName())) {
                    boolean targetFieldAccessible = targetField.isAccessible();
                    boolean sourcefieldAccessible = sourcefield.isAccessible();
                    try {
                        if (!Modifier.isPublic(sourcefield.getModifiers())) {
                            sourcefield.setAccessible(true);
                        }
                        Object sourceFiledValue = sourcefield.get(source);
    
                        if (!Modifier.isPublic(targetField.getModifiers())) {
                            targetField.setAccessible(true);
                        }
                        if (sourceFiledValue != null) {
                            targetField.set(target,sourcefield.get(source));
                        }
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        targetField.setAccessible(targetFieldAccessible);
                        sourcefield.setAccessible(sourcefieldAccessible);
                    }
                }
            }
        }
    }
    
}
