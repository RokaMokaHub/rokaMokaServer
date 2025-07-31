package br.edu.ufpel.rokamoka.config.broker;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author MauricioMucci
 */
@Getter
@Component
public class RabbitMQExchangeConfigProperties {

    @Value("${broker.exchange.emblems}") private String emblems;
}
