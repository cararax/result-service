package com.carara.result.infra.message;

import com.carara.result.infra.message.response.Vote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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

    ObjectMapper newObjectMapper = new ObjectMapper();

    public void listen(Long agendaId) throws JsonProcessingException {
        log.info(" [x] Requesting result for " + agendaId);

        String reponse = (String) template.convertSendAndReceive(exchange.getName(), routingKey, agendaId);
        List<Vote> voteList= newObjectMapper.readValue(reponse, List.class);

        log.info(" [.] Returned " + voteList);
    }
}
