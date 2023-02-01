package com.carara.result.infra.message.config;

import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

public class CustomFatalExceptionStrategy
      extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
    @Override
    public boolean isFatal(Throwable t) {
        return (t.getCause() instanceof AmqpIllegalStateException);
    }
}