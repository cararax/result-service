package com.carara.result.infra.message;

import com.carara.result.domain.Result;
import com.carara.result.infra.message.response.Vote;
import com.carara.result.service.ResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ResultPublisher {
//SERVER

    @Autowired
    ResultService resultService;

    ObjectMapper newObjectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Autowired
    VoteListener voteListener;

    @RabbitListener(queues = "${rabbitmq.result.queue}")
    public String publish(Long agendaId) throws JsonProcessingException {
        log.info(" [2] Received request for agenda "+ agendaId);

        String response = voteListener.listen(agendaId);
        //todo: verificar se a lista está vazia
        if (response == null) {
            throw new AmqpIllegalStateException("Impossible to get votes, try again later.");
        }
        //todo: atenção na desserialização
        List<Vote> voteList = newObjectMapper.readValue(response, new TypeReference<List<Vote>>() {
        });

        Result result = resultService.calculateResult(voteList);

        String json = newObjectMapper.writeValueAsString(result);
        if (json == null) {
            throw new AmqpIllegalStateException("Impossible to get votes, try again later.");
        }
        //todo: calculate result
        //todo:return result
        //todo: save result

        log.info(" [7] Returned result for agenda "+agendaId +", data: " + json);
        return json;
    }
}
