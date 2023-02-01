package com.carara.result.infra.message;

import com.carara.result.domain.Result;
import com.carara.result.infra.message.response.Vote;
import com.carara.result.service.ResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Log4j2(topic = "ResultPublisher")
public class ResultPublisher {
    @Autowired
    ResultService resultService;

    ObjectMapper newObjectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    VoteListener voteListener;

    @RabbitListener(queues = "${rabbitmq.result.queue}")
    public String publish(Long agendaId) throws JsonProcessingException {
        log.info("Loading votes for agenda " + agendaId);
        String response = voteListener.listen(agendaId);
        if (response == null) {
            log.error("Impossible to get votes for agenda " + agendaId);
            throw new AmqpIllegalStateException("Impossible to get votes, try again later.");
        }
        List<Vote> voteList = newObjectMapper.readValue(response, new TypeReference<List<Vote>>() {
        });

        Result result = resultService.calculateResult(voteList);

        String json = newObjectMapper.writeValueAsString(result);
        if (json == null) {
            log.error("Votes not found for agenda: " + agendaId);
            throw new AmqpIllegalStateException("Impossible to get votes, try again later.");
        }
        log.info("Returned result for agenda " + agendaId + ", result: " + json);
        return json;
    }
}
