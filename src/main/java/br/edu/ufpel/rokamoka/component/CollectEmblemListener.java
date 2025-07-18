package br.edu.ufpel.rokamoka.component;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.emblem.IEmblemService;
import br.edu.ufpel.rokamoka.service.mokadex.MokadexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author MauricioMucci
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CollectEmblemListener {

    private final IEmblemService emblemService;
    private final MokadexService mokadexService;

    @RabbitListener(queues = "#{@rabbitMQQueueConfigProperties.collectEmblem}")
    public void onCollectStar(CollectEmblemDTO collectEmblemDTO) {
        log.info("Listener coletar emblema acionado para [{}]", collectEmblemDTO);

        emblemService
                .findByExhibitionId(collectEmblemDTO.exhibitionId())
                .ifPresent(emblem -> collectEmblem(collectEmblemDTO.mokadexId(), emblem));

        log.info("Listener coletar emblema finalizado para [{}]", collectEmblemDTO);
    }

    private void collectEmblem(Long mokadexId, Emblem emblem) {
        try {
            this.mokadexService.collectEmblem(mokadexId, emblem);
        } catch (RokaMokaContentDuplicatedException | RokaMokaContentNotFoundException e) {
            throw new AmqpRejectAndDontRequeueException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
