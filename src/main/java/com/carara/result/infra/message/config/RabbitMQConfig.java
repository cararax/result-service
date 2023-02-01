package com.carara.result.infra.message.config;


import com.carara.result.infra.message.ResultPublisher;
import com.carara.result.infra.message.VoteListener;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
public class RabbitMQConfig {
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

    @Value("${rabbitmq.result.queue}")
    private String resultQueue;
    @Value("${rabbitmq.result.exchange}")
    private String resultExchange;
    @Value("${rabbitmq.result.routingkey}")
    private String resultRoutingKey;

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(customExceptionStrategy());
    }

    @Bean
    FatalExceptionStrategy customExceptionStrategy() {
        return new CustomFatalExceptionStrategy();
    }

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
