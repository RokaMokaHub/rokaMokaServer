package br.edu.ufpel.rokamoka.component;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.service.emblem.IEmblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author MauricioMucci
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateEmblemListener {

    private final IEmblemService emblemService;
    private final MokadexRepository mokadexRepository;

    @RabbitListener(queues = "emblems.v1.create-emblem")
    public void onCollectStar(CollectEmblemDTO collectEmblemDTO) {
        log.info("Listener coletar emblema acionado para [{}]", collectEmblemDTO);

        Optional<Emblem> maybeEmblem = emblemService.collectEmblem(collectEmblemDTO);
        if (maybeEmblem.isEmpty()) {
            return;
        }

        Emblem emblem = maybeEmblem.get();
        Mokadex mokadex = mokadexRepository.findById(collectEmblemDTO.mokadexId())
                .orElseThrow(() -> new ServiceException(""));

        if (!mokadex.addEmblem(emblem)) {
            throw new ServiceException("Erro ao coletar emblema na mokadex");
        }

        log.info("Emblema coletado com sucesso!");
        mokadex = mokadexRepository.save(mokadex);
    }
}
