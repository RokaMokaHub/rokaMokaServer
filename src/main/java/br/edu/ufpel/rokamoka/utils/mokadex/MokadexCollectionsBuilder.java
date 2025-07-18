package br.edu.ufpel.rokamoka.utils.mokadex;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.CollectionDTO;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Responsible for constructing and managing sets of {@link CollectionDTO} objects based on relationships between
 * artworks and exhibitions in the {@link Mokadex} resource.
 *
 * @author MauricioMucci
 * @see CollectionDTO
 */
public class MokadexCollectionsBuilder {

    private final Mokadex mokadex;

    public MokadexCollectionsBuilder(Mokadex mokadex) {
        this.mokadex = mokadex;
    }

    /**
     * Constructs a set of {@link CollectionDTO} objects based on the relationships between artworks and their
     * associated exhibitions in the {@code mokadex}.
     *
     * @return A {@link Set} of {@link CollectionDTO} objects, each representing an exhibition and its associated
     * artworks. The set is unmodifiable.
     */
    public Set<CollectionDTO> buildCollectionSet() {
        Set<Artwork> artworkSet = this.mokadex.getArtworks();
        Map<ExhibitionOutputDTO, Set<ArtworkOutputDTO>> artworksByExhibition = mapArtworksToExhibition(artworkSet);
        return buildCollectionSetByMap(artworksByExhibition);
    }

    /**
     * Maps a set of {@link Artwork} objects to a {@link Map} where the keys represent exhibitions and the values are
     * sets of artworks associated with those exhibitions.
     *
     * @param artworkSet The {@link Set} of {@link Artwork} objects to be processed.
     *
     * @return A {@link Map} where the keys are {@link ExhibitionOutputDTO} objects representing exhibitions, and the
     * values are {@link Set} objects containing {@link ArtworkOutputDTO} representations of the artworks associated
     * with each exhibition. If no artworks are associated with any exhibitions, the map will be empty.
     */
    private Map<ExhibitionOutputDTO, Set<ArtworkOutputDTO>> mapArtworksToExhibition(Set<Artwork> artworkSet) {
        return artworkSet.stream()
                .filter(artwork -> artwork.getExhibition() != null)
                .collect(Collectors.groupingBy(
                        artwork -> new ExhibitionOutputDTO(artwork.getExhibition()),
                        Collectors.mapping(ArtworkOutputDTO::new, Collectors.toSet())
                ));
    }

    /**
     * Builds a set of {@link CollectionDTO} objects based on a mapping between exhibitions and their associated
     * artworks.
     *
     * @param artworksByExhibition A {@link Map} where the keys are {@link ExhibitionOutputDTO} objects representing
     * exhibitions, and the values are {@link Set}s of {@link ArtworkOutputDTO} objects representing artworks associated
     * with each exhibition.
     *
     * @return A {@link Set} of {@link CollectionDTO} objects, where each object represents an exhibition and its
     * associated artworks. The set is guaranteed to be unmodifiable.
     */
    private Set<CollectionDTO> buildCollectionSetByMap(
            Map<ExhibitionOutputDTO, Set<ArtworkOutputDTO>> artworksByExhibition) {
        return artworksByExhibition.entrySet()
                .stream()
                .map(entry -> new CollectionDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
