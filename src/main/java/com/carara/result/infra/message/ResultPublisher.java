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
        log.info(" [x] Received request for agenda ");
        //todo: chamar o vote para pegar a lista de votos
        String listen = voteListener.listen(agendaId);
//todo: atenção na desserialização
        List<Vote> voteList= newObjectMapper.readValue(listen, new TypeReference<List<Vote>>() {
        });
        Result result = resultService.calculateResult(voteList);
        String json = newObjectMapper.writeValueAsString(result);

        //todo: calculate result
        //todo:return result
        //todo: save result

        log.info(" [.] Returned result for agenda "+ json);
        return json;
    }
}
