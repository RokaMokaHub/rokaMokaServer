package br.edu.ufpel.rokamoka.config.broker;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author MauricioMucci
 */
@Getter
@Component
public class RabbitMQQueueConfigProperties {

    @Value("${broker.queue.collect-emblem}") private String collectEmblem;
}
