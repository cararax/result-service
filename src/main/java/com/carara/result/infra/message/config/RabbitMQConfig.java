package com.carara.result.infra.message.config;


import com.carara.result.infra.message.ResultPublisher;
import com.carara.result.infra.message.VoteListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //Client
    @Value("${rabbitmq.vote.exchange}")
    public String voteExchangeName;
    @Bean
    public DirectExchange voteExchange() {
        return new DirectExchange(voteExchangeName);
    }
    @Bean
    public VoteListener client() {
        return new VoteListener();
    }

    //SERVER
    @Value("${rabbitmq.result.queue}")
    private String resultQueue;
    @Value("${rabbitmq.result.exchange}")
    private String resultExchange;
    @Value("${rabbitmq.result.routingkey}")
    private String resultRoutingKey;

    @Bean
    public Queue queue() {
        return new Queue(resultQueue);
    }
    @Bean
    public DirectExchange resultExchange() {
        return new DirectExchange(resultExchange);
    }
    @Bean
    public Binding binding(DirectExchange resultExchange, Queue resultQueue) {
        return BindingBuilder.bind(resultQueue).to(resultExchange).with(resultRoutingKey);
    }
    @Bean
    public ResultPublisher server() {
        return new ResultPublisher();
    }
}
