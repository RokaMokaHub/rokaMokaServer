package br.edu.ufpel.rokamoka.component;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.service.emblem.IEmblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author MauricioMucci
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CollectEmblemListener {

    private final IEmblemService emblemService;

    @RabbitListener(queues = "#{@rabbitMQQueueConfigProperties.collectEmblem}")
    public void onCollectStar(CollectEmblemDTO collectEmblemDTO) {
        log.info("Listener coletar emblema acionado para [{}]", collectEmblemDTO);

        Optional<Emblem> maybeEmblem = emblemService.collectEmblem(collectEmblemDTO);
        maybeEmblem.ifPresentOrElse(
                emblem -> log.info("[{}] coletado com sucesso", emblem),
                () -> log.info("[{}] não foi coletado", Emblem.class.getSimpleName())
                );

        log.info("Listener coletar emblema finalizado para [{}]", collectEmblemDTO);
    }
}
