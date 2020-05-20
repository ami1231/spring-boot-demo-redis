package com.ami.redisdemo.message.send;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageService {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    public void sendDataToAmi1(){
        redisTemplate.convertAndSend("ami1.channel","Hello Ami1");
    }

    public void sendDataToAmi2(){
        redisTemplate.convertAndSend("ami2.channel","Hello Ami2");
    }

}
