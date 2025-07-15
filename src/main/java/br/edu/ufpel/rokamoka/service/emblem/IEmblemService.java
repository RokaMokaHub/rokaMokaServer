package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import jakarta.validation.Valid;

import java.util.Optional;

/**
 * @author MauricioMucci
 */
public interface IEmblemService {

    Optional<Emblem> collectEmblem(@Valid CollectEmblemDTO collectEmblemDTO);

}
