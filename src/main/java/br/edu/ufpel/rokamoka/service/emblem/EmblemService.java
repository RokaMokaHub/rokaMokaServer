package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.emblem.CollectEmblemDTO;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * @author MauricioMucci
 */
@Service
@Validated
@RequiredArgsConstructor
public class EmblemService implements IEmblemService {

    private final EmblemRepository emblemRepository;
    private final MokadexRepository mokadexRepository;
    private final ArtworkRepository artworkRepository;

    @Override
    public Optional<Emblem> collectEmblem(@Valid CollectEmblemDTO collectEmblemDTO) {
        Optional<Mokadex> maybeMokadex = mokadexRepository.findById(collectEmblemDTO.mokadexId());
        Optional<Artwork> maybeArtwork = artworkRepository.findById(collectEmblemDTO.newlyAddedArtworkId());
        if (maybeMokadex.isEmpty() || maybeArtwork.isEmpty()) {
            return Optional.empty();
        }

        if (emblemRepository.hasCollectedAllArtworksInExhibition(
                collectEmblemDTO.mokadexId(), collectEmblemDTO.newlyAddedArtworkId())) {
            return emblemRepository.findEmblemByArtworkId(collectEmblemDTO.newlyAddedArtworkId());
        }

        return Optional.empty();
    }
}
