package com.carara.result.infra.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Log4j2(topic = "VoteListener")
public class VoteListener {

    //CLIENT
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange voteExchange;

    @Value("${rabbitmq.vote.routingkey}")
    private String voteRoutingKey;


    ObjectMapper newObjectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    public String listen(Long agendaId) throws JsonProcessingException {
        log.info("Requesting votes for agendaId: " + agendaId);
        String response = (String) template.convertSendAndReceive(voteExchange.getName(), voteRoutingKey, agendaId);
        if (response == null) {
            log.error("Votes not found for agenda: " + agendaId);
            throw new AmqpIllegalStateException("Impossible to get votes, try again later.");
        }
        log.info("Returning found votes for agendaId: " + agendaId + ", votes: " + response);
        return response;
    }
}
