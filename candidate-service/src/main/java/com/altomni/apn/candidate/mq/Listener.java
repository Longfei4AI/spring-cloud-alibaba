package com.altomni.apn.candidate.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author longfeiwang
 */
@Slf4j
@Component
public class Listener {
    @RabbitListener(queues = "candidate-queue")
    public void listenCandidateQueueMessage(String msg) {
        log.info("Get message from candidate queue: {}", msg);
    }
}
