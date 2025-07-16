package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for managing and retrieving information related to {@link Mokadex}.
 *
 * @author mauri
 * @see MokadexService
 */
@Validated
public interface IMokadexService {

    Mokadex getOrCreateMokadexByUser(@Valid User usuario);

    MokadexOutputDTO buildMokadexOutputDTOByMokadex(@Valid Mokadex mokadex);

    MokadexOutputDTO collectStar(@NotBlank String qrCode) throws RokaMokaContentNotFoundException;
}
