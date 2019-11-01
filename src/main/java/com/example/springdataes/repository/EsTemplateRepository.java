package com.example.springdataes.repository;

import com.example.springdataes.utils.ReflectUtil;
import com.google.common.collect.Lists;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzj
 */
@Component
public class EsTemplateRepository<T> {
    
    @Autowired
    protected ElasticsearchTemplate elasticsearchTemplate;
    
    /**
     * 更新/插入
     *
     * @param list 对象集合
     * @return 更新/插入数量
     * @throws Exception 异常
     */
    public int upSert(List<T> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        
        // 验证对象是否有唯一标识
        T entity = list.get(0);
        Field id = null;
        for (Field field : entity.getClass().getDeclaredFields()) {
            Id businessID = field.getAnnotation(Id.class);
            if (businessID != null) {
                id = field;
                break;
            }
        }
        if (id == null) {
            throw new Exception("Can't find @Id on " + entity.getClass().getName());
        }
        
        Document document = ReflectUtil.getDocument(entity.getClass());
        List<UpdateQuery> updateQueries = new ArrayList<>();
        for (T obj : list) {
            UpdateQuery updateQuery = new UpdateQuery();
            updateQuery.setIndexName(document.indexName());
            updateQuery.setType(document.type());
            updateQuery.setId(ReflectUtil.getFieldValue(id, obj).toString());
            // 插入
            IndexRequest indexRequest = new IndexRequest(updateQuery.getIndexName(), updateQuery.getType(), updateQuery.getId())
                    .source(ReflectUtil.Obj2Map(obj, true));
            // 更新
            UpdateRequest updateRequest = new UpdateRequest(updateQuery.getIndexName(), updateQuery.getType(), updateQuery.getId())
                    .doc(ReflectUtil.Obj2Map(obj, false))
                    .upsert(indexRequest);
            
            updateQuery.setUpdateRequest(updateRequest);
            updateQuery.setClazz(obj.getClass());
            updateQueries.add(updateQuery);
        }
        
        if (!CollectionUtils.isEmpty(updateQueries)) {
            elasticsearchTemplate.bulkUpdate(updateQueries);
        }
        
        return list.size();
    }
    
    /**
     * 单个更新/插入
     *
     * @param obj 数据
     * @return int
     * @throws Exception 异常
     */
    public int upSert(T obj) throws Exception {
        List<T> objs = Lists.newArrayList();
        objs.add(obj);
        return upSert(objs);
    }
}
