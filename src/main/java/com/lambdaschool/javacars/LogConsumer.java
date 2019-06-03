package com.lambdaschool.javacars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Service
public class LogConsumer {

    @RabbitListener(queues = JavacarsApplication.QUEUE_NAME)
    public void consumeMessage(final Message cm){
        log.info("Received message: {}", cm.toString());
    }


}
