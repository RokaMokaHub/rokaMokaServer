package br.edu.ufpel.rokamoka.component;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.emblem.IEmblemService;
import br.edu.ufpel.rokamoka.service.mokadex.IMokadexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listener class for processing emblem collection events from a RabbitMQ queue.
 *
 * @author MauricioMucci
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CollectEmblemConsumer {

    private final IEmblemService emblemService;
    private final IMokadexService mokadexService;

    /**
     * Processes a message from a RabbitMQ queue to handle emblem collection events.
     *
     * @param collectEmblemDTO The data transfer object containing the necessary IDs for identifying and collecting the
     * emblem.
     */
    @RabbitListener(queues = "#{@rabbitMQQueueConfigProperties.collectEmblem}")
    public void consumeCollectEmblem(CollectEmblemDTO collectEmblemDTO) {
        log.info("Listener coletar emblema acionado para [{}]", collectEmblemDTO);

        emblemService
                .findByExhibitionId(collectEmblemDTO.exhibitionId())
                .ifPresent(emblem -> tryToCollectEmblem(collectEmblemDTO.mokadexId(), emblem));

        log.info("Listener coletar emblema finalizado para [{}]", collectEmblemDTO);
    }

    private void tryToCollectEmblem(Long mokadexId, Emblem emblem) {
        try {
            this.mokadexService.collectEmblem(mokadexId, emblem);
        } catch (RokaMokaContentDuplicatedException | RokaMokaContentNotFoundException e) {
            throw new AmqpRejectAndDontRequeueException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
