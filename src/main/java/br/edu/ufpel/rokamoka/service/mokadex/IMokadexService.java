package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import jakarta.validation.constraints.NotBlank;

/**
 * Service interface for managing and retrieving information related to {@link Mokadex}.
 *
 * @author MauricioMucci
 * @see MokadexService
 */
public interface IMokadexService {

    Mokadex getOrCreateMokadexByUser(User usuario);

    MokadexOutputDTO buildMokadexOutputDTOByMokadex(Mokadex mokadex);

    Mokadex collectStar(@NotBlank String qrCode)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException;

    Mokadex collectEmblem(Long mokadexId, Emblem emblem)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException;
}
