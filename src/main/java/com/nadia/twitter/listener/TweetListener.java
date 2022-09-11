package com.nadia.twitter.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class TweetListener {

    private final ObjectMapper objectMapper;

    public TweetListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @RabbitListener(queues = "tweet.create")
    public void listen(Message message) throws IOException {

        TweetMessage tweetMessage = objectMapper.readValue(message.getBody(), TweetMessage.class);

        log.info("message received : {}", tweetMessage.getContent());


    }
}
