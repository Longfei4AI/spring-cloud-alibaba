package com.altomni.apn.candidate.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * @author longfeiwang
 */
@Service
public class RedisService {
    @Resource
    private RedisTemplate redisTemplate;

    /*
     Value operations
     */

    public void setForValue(Object key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public void setForValue(Object key, Object value, Duration timeout){
        // e.g. timeout = Duration.of(3, ChronoUnit.DAYS);
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public Object getForValue(Object key){
        return redisTemplate.opsForValue().get(key);
    }

    /*
     Set operations
     */

    public void addToSet(Object key, Object... value){
        redisTemplate.opsForSet().add(key, value);
    }

    public void addToSet(Object key, Duration timeout, Object... value){
        // e.g. timeout = Duration.of(3, ChronoUnit.DAYS);
        redisTemplate.opsForSet().add(key, value, timeout);
    }

    public Object popOneFromSet(Object key){
        return redisTemplate.opsForSet().pop(key);
    }

    public List popMultipleFromSet(Object key, long size){
        return redisTemplate.opsForSet().pop(key, size);
    }

    public List popAllFromSet(Object key){
        return redisTemplate.opsForSet().pop(key, redisTemplate.opsForSet().size(key));
    }

    /*
     List operations
     */

    public void pushOneToList(Object key, Object value){
        redisTemplate.opsForList().leftPush(key, value);
    }

    public void pushOneToList(Object key, Duration timeout, Object value){
        // e.g. timeout = Duration.of(3, ChronoUnit.DAYS);
        redisTemplate.opsForList().leftPush(key, value, timeout);
    }

    public void pushAllToList(Object key, List value){
        redisTemplate.opsForList().leftPushAll(key, value);
    }

    public void pushAllToList(Object key, Duration timeout, List value){
        // e.g. timeout = Duration.of(3, ChronoUnit.DAYS);
        redisTemplate.opsForList().leftPushAll(key, value, timeout);
    }

    public Object popOneFromList(Object key){
        return redisTemplate.opsForList().rightPop(key);
    }

    public List popMultipleFromList(Object key, long size){
        return redisTemplate.opsForList().range(key, 0, 0 + size);
    }

    public List popAllFromList(Object key){
        return redisTemplate.opsForList().range(key, 0, redisTemplate.opsForList().size(key));
    }
}
