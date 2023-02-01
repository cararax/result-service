package com.carara.result.infra.message;

import com.carara.result.infra.message.response.Vote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Slf4j
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
        log.info(" [3] Requesting votes from result, agenda " + agendaId);

        String response = (String) template.convertSendAndReceive(voteExchange.getName(), voteRoutingKey, agendaId);
        if (response == null) {
            throw new AmqpIllegalStateException("Impossible to get votes, try again later.");
        }
//        String json = newObjectMapper.writeValueAsString(reponse);


        log.info(" [6] Returning response from result, agenda " + agendaId + ", data: " + response);
        return response;

    }
}
