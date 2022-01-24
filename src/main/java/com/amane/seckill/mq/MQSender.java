package com.amane.seckill.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendMS(String msg){
        log.info(msg);
        rabbitTemplate.convertAndSend("msExchange","seckill.msMessage",msg);
    }
}
