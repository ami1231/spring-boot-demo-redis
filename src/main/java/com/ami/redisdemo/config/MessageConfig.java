package com.ami.redisdemo.config;

import com.ami.redisdemo.message.receive.AmiReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

@Configuration
public class MessageConfig {
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(ami1MessageListener(), new PatternTopic("ami1.channel*"));
        container.addMessageListener(ami2MessageListener(), new ChannelTopic("ami2.channel"));
        container.addMessageListener(ami3MessageListener(), new PatternTopic("ami1.channel*"));
        return container;
    }

    @Bean
    MessageListenerAdapter ami1MessageListener() {
        return new MessageListenerAdapter(new AmiReceiver(), "receiverAmi1");
    }

    @Bean
    MessageListenerAdapter ami2MessageListener() {
        return new MessageListenerAdapter(new AmiReceiver(), "receiverAmi1");
    }

    @Bean
    MessageListenerAdapter ami3MessageListener() {
        return new MessageListenerAdapter(new AmiReceiver(), "receiverAmi3");
    }


}