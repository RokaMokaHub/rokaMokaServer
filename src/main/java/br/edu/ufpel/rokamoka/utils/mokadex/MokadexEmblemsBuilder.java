package br.edu.ufpel.rokamoka.utils.mokadex;

import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Responsible for constructing and managing sets of {@link EmblemOutputDTO} objects based on relationships in the
 * {@link Mokadex} resource.
 *
 * @author MauricioMucci
 * @see EmblemOutputDTO
 */
public class MokadexEmblemsBuilder {

    private final Mokadex mokadex;

    public MokadexEmblemsBuilder(Mokadex mokadex) {
        this.mokadex = mokadex;
    }

    /**
     * Constructs a set of {@link EmblemOutputDTO} objects based on the relationships in the {@code mokadex}.
     *
     * @return A {@link Set} of {@link EmblemOutputDTO} objects, each representing an exhibition and its associated
     * artworks. The set is unmodifiable.
     */
    public Set<EmblemOutputDTO> buildEmblemSet() {
        return this.mokadex.getEmblems()
                .stream()
                .map(EmblemOutputDTO::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
