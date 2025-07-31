package br.edu.ufpel.rokamoka.component;

import br.edu.ufpel.rokamoka.config.broker.RabbitMQExchangeConfigProperties;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Producer class responsible for publishing emblem collection events to a RabbitMQ exchange.
 *
 * @author MauricioMucci
 */
@Component
@RequiredArgsConstructor
public class CollectEmblemProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQExchangeConfigProperties exchangeConfigProperties;

    /**
     * Publishes an emblem collection event to a RabbitMQ exchange.
     *
     * @param mokadex The {@link Mokadex} object whose ID identifies the source of the emblem collection request.
     * @param exhibition The {@link Exhibition} object whose ID identifies the target exhibition for the emblem collection.
     */
    public void publishCollectEmblem(Mokadex mokadex, Exhibition exhibition) {
        CollectEmblemDTO collectEmblemDTO = new CollectEmblemDTO(mokadex.getId(), exhibition.getId());
        this.rabbitTemplate.convertAndSend(
                this.exchangeConfigProperties.getEmblems(),
                "",
                collectEmblemDTO
        );
    }
}
