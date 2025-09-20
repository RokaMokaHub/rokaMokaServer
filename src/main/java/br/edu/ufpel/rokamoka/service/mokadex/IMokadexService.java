package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * Service interface for managing and retrieving information related to {@link Mokadex}.
 *
 * @author MauricioMucci
 * @see MokadexService
 */
@Validated
public interface IMokadexService {

    Mokadex findById(@NotNull Long mokadexId) throws RokaMokaContentNotFoundException;

    Mokadex getOrCreateMokadexByUser(@NotNull User usuario);

    MokadexOutputDTO getMokadexOutputDTOByMokadex(@NotNull Mokadex mokadex);

    Mokadex collectStar(@NotBlank String qrCode)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException;

    Mokadex collectEmblem(Long mokadexId, Emblem emblem)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException;

    Set<Artwork> getMissingStarsByExhibition(@NotNull Long exhibitionId) throws RokaMokaContentNotFoundException;
}
