package com.carara.result.infra.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
public class ResultPublisher {

//    @Autowired
//    private VoteService voteService;
//    ObjectMapper newObjectMapper = JsonMapper.builder()
//            .addModule(new JavaTimeModule())
//            .build();

    //SERVER
    @RabbitListener(queues = "${rabbitmq.result.queue}")
    public String publish() throws JsonProcessingException {
        log.info(" [x] Received request for agenda ");


        log.info(" [.] Returned ");
        return "teste result publisher";
    }
}
