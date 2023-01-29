package com.carara.result.infra.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResultListener {

    //CLIENT
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public void listen(Long agendaId) {
        log.info(" [x] Requesting result for " + agendaId);
        //todo: create vote class
        log.info((String) template.convertSendAndReceive(exchange.getName(), routingKey, agendaId));
    }
}
