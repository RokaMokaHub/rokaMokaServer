package br.edu.ufpel.rokamoka.utils.mokadex;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.CollectionDTO;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Responsible for constructing and managing sets of {@code CollectionDTO} objects based on relationships between
 * artworks and exhibitions in the {@code Mokadex} resource.
 *
 * @author MauricioMucci
 * @see CollectionDTO
 * @see Mokadex
 */
public record MokadexCollectionsBuilder(Mokadex mokadex) {

    /**
     * Constructs a set of {@code CollectionDTO} objects based on the relationships between artworks and their
     * associated exhibitions in the {@code mokadex}.
     *
     * @return An unmodifiable {@code Set} of {@code CollectionDTO} objects.
     */
    public Set<CollectionDTO> buildCollectionSet() {
        Set<Artwork> artworkSet = this.mokadex.getArtworks();

        Map<Exhibition, Set<ArtworkOutputDTO>> artworksByExhibition = artworkSet.stream()
                .collect(Collectors.groupingBy(Artwork::getExhibition,
                        Collectors.mapping(ArtworkOutputDTO::new, Collectors.toSet())));

        return artworksByExhibition.entrySet().stream().map(entry -> {
            Exhibition exhibition = entry.getKey();
            Set<ArtworkOutputDTO> artworkDTOs = entry.getValue();

            long totalArtworks = artworkDTOs.size();
            ExhibitionOutputDTO exhibitionDTO = new ExhibitionOutputDTO(exhibition, totalArtworks);

            return new CollectionDTO(exhibitionDTO, artworkDTOs);
        }).collect(Collectors.toUnmodifiableSet());
    }
}
