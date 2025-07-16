package br.edu.ufpel.rokamoka.config.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MauricioMucci
 */
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    @Bean
    public FanoutExchange emblemsV1FanoutExchange(RabbitMQExchangeConfigProperties properties) {
        return new FanoutExchange(properties.getEmblems());
    }

    @Bean
    public Queue emblemsV1CollectQueue(RabbitMQQueueConfigProperties properties) {
        return new Queue(properties.getCollectEmblem());
    }

    @Bean
    public Binding collectEmblemBinding(Queue emblemsV1CollectQueue, FanoutExchange emblemsV1FanoutExchange) {
        return BindingBuilder.bind(emblemsV1CollectQueue).to(emblemsV1FanoutExchange);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
            RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }
}
