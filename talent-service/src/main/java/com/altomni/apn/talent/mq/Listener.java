package com.altomni.apn.talent.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author longfeiwang
 */
@Slf4j
@Component
public class Listener {

    private static String[] INITIAL_QUEUE_LIST = {"candidate-queue", "job-queue"};

    @Resource
    private ConnectionFactory connectionFactory;

    @PostConstruct
    public void initQueue(){
        AmqpAdmin amqpAdmin = new RabbitAdmin(connectionFactory);
        for (String queueName : INITIAL_QUEUE_LIST) {
            amqpAdmin.declareQueue(new Queue(queueName));
        }
    }

    @RabbitListener(queues = "candidate-queue")
    public void listenCandidateQueueMessage(String msg) {
        log.info("Get message from candidate queue: {}", msg);
    }
}
