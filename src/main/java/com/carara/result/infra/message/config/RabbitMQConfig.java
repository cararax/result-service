package com.carara.result.infra.message.config;


import com.carara.result.infra.message.VoteListener;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //Client
    @Value("${rabbitmq.exchange}")
    public String exchangeName;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public VoteListener client() {
        return new VoteListener();
    }

}
