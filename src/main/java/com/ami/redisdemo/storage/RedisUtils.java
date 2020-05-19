package com.ami.redisdemo.storage;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public void set( final String key,  final Long expireTime,  final TimeUnit timeUnit,  final Object obj) {
         redisTemplate.opsForValue().set(key,obj,expireTime,timeUnit);
    }

    /**
     * 根據key取得單一物件
     * @param key
     * @param classType
     * @param <T>
     * @return
     */
    public <T> T get(final String key , final Class<T> classType){
        Object value = redisTemplate.opsForValue().get(key);
        if(value!=null){
            return classType.cast(value);
        }
        return null;
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean delKey( final String key) {
        return redisTemplate.delete(key);
    }

    /**
     *
     * @param key
     * @param amount
     * @return
     */
    public Long incr( final String key,  final Long amount) {
        return redisTemplate.boundValueOps(key).increment(amount);
    }


    /**
     *
     * @param key
     * @param hKey
     * @param obj
     * @param <T>
     */
    public <T> void hSet( final String key,  final Object hKey,  final T obj) {
        redisTemplate.opsForHash().put(key, hKey, obj);
    }


    /**
     * hash get
     * @param key
     * @param hKey
     */
    public <T> T hGet( final String key,  final Object hKey, final Class<T> cls){
        Object object  = redisTemplate.opsForHash().get(key, hKey);
        if(object!=null){
            return cls.cast(object);
        }else{
            return null;
        }
    }

    /**
     *
     * @param key
     * @param hKeys
     */
    public void hdel(final String key, final Object... hKeys){
        redisTemplate.opsForHash().delete(key,hKeys);
    }

    /**
     *
     * @param key
     * @param hKey
     * @param amount
     */
    public void hincr(final String key,final Object hKey ,final Long amount){
        redisTemplate.opsForHash().increment(key,hKey,amount);
    }


    /**
     *
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean expire( final String key,  final Long timeout,  final TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 拿取分片集合
     * @param pattern 要查的patter
     * @param count   每次要的筆數
     * @return
     */
    public Set<String> scan(final String pattern, long count) {

        if(count <= 0){
            throw new RuntimeException("count is too small");
        }
        return redisTemplate.execute((RedisCallback<Set<String>>) redisConnection -> {
            Set<String> keys = new HashSet<>();
            try{
                Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions().match(pattern).count(count).build());
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
                if(Objects.nonNull(cursor) && !cursor.isClosed()){
                    cursor.close();
                }
            }catch (Exception e){
                System.out.println("redis scan error");
            }
            return keys;
        });
    }

    /**
     * 透過分布式鎖取得
     * @param key
     * @param expireTime
     * @param timeUnit
     * @return
     */
    public String setLock(final String key,  final Long expireTime,  final TimeUnit timeUnit){
        String uuid = UUID.randomUUID().toString();
        Boolean isSuccess= redisTemplate.opsForValue().setIfPresent(key,uuid,expireTime,timeUnit);
        if (Objects.isNull(isSuccess) || !isSuccess) {
            return null;
        }
        return uuid;
    }

}
