package com.testWorkForAlfa.app;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MongoTemplate mongoTemplate;

    public CacheService(RedisTemplate<String, Object> redisTemplate, MongoTemplate mongoTemplate) {
        this.redisTemplate = redisTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    public Object getFromCache(String cacheKey) {
        return redisTemplate.opsForValue().get(cacheKey);
    }

    public void saveToCache(String cacheKey, Object result) {
        redisTemplate.opsForValue().set(cacheKey, result);
    }

    public void saveToMongo(String methodName, Object result, Object[] args) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("methodName", methodName);
        logData.put("args", Arrays.toString(args));
        logData.put("result", result);
        mongoTemplate.save(logData, "requestLogs");
    }
}

