package br.edu.ufpel.rokamoka.config.broker;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author MauricioMucci
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "broker.exchange")
public class RabbitMQExchangeConfigProperties {

    private String emblems;
}
