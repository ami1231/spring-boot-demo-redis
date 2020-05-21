package com.ami.redisdemo.storage;


import com.ami.redisdemo.message.send.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisUtilTest {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private MessageService messageService;

    //一般資料使用
    @Test
    public void testSet(){
        //使用redis時,都須考慮到設置合理的 ttl時間,以避免資料永久存放
        redisUtils.set("amitest" ,10l,TimeUnit.SECONDS,"hello world");
        System.out.println(redisUtils.get("amitest",String.class));
    }

    //hash數據結構使用
    @Test
    public void testHash(){
        String key = "ami-hash";
        redisUtils.hSet(key,"01",10);
        redisUtils.hSet(key,"02",20);
        redisUtils.hSet(key,"03",30);
        redisUtils.hSet(key,"04",40);

        System.out.println(redisUtils.hGet(key,"02",Integer.class));
    }

    @Test
    public void testLock() throws InterruptedException {

        String keyTest = "ami-lock-test";
        String key = "ami-lock";
        redisUtils.delKey(keyTest);
        redisUtils.set(keyTest,1l,TimeUnit.MINUTES,0);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Boolean isLock = redisUtils.setLock(key,1l,TimeUnit.MINUTES);
                if(isLock){
                    System.out.println(Thread.currentThread().getId()+" get lock success");
                    redisUtils.incr(keyTest,1l);
                }else{
                    System.out.println(Thread.currentThread().getId()+" get lock fail");
                }

            }
        };

        for(int i=0;i<10;i++){
            new Thread(runnable).start();
        }

        Thread.currentThread().join(5000);

        System.out.println(redisUtils.get(keyTest,Integer.class));
    }

    @Test
    public void testMessage(){
        //topic
        messageService.sendDataToAmi1();
        //channel
        messageService.sendDataToAmi2();
    }

}
