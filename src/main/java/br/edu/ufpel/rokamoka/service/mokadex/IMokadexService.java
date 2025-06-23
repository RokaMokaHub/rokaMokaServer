package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Service interface for managing and retrieving information related to {@link Mokadex}.
 *
 * @author mauri
 * @see MokadexService
 */
@Validated
public interface IMokadexService {

    Optional<Mokadex> getMokadexByUsuario(@Valid User usuario);

    MokadexOutputDTO buildMokadexOutputDTOByMokadex(@Valid Mokadex mokadex);
}
