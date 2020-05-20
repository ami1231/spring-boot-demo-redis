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

import java.util.*;

@Configuration
public class MessageConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }

    @Bean
    public List<MessageListenerAdapter> messageListenerAdapter(AmiReceiver amiReceiver){
        List<MessageListenerAdapter> adapterList = new ArrayList<>();
        Map<String,MessageListenerAdapter> adapterMap = new HashMap<>();
        adapterList.add(new MessageListenerAdapter(amiReceiver,"receiverAmi1"));
        adapterList.add(new MessageListenerAdapter(amiReceiver,"receiverAmi2"));
        return adapterList;
    }

    private Collection<? extends Topic> getTopics(){

        Topic[] topics = new Topic[]{
            new PatternTopic("ami1.channel*"),
            new ChannelTopic("ami2.channel"),
        };
        return Arrays.asList(topics);
    }

}