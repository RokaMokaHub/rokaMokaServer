package br.edu.ufpel.rokamoka.utils.exhibition;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;

/**
 * A builder class responsible for constructing instances of {@code Exhibition}.
 *
 * @author MauricioMucci
 * @see Exhibition
 * @see Location
 * @see ExhibitionInputDTO
 */
public final class ExhibitionBuilder {

    private Long existingId;
    private final ExhibitionInputDTO input;
    private final Location location;

    public ExhibitionBuilder(Long existingId, ExhibitionInputDTO input, Location location) {
        this.existingId = existingId;
        this.input = input;
        this.location = location;
    }

    public ExhibitionBuilder(ExhibitionInputDTO input, Location location) {
        this.input = input;
        this.location = location;
    }

    public Exhibition build() {
        Exhibition exhibition = Exhibition.builder()
                .name(this.input.name())
                .description(this.input.description())
                .location(this.location)
                .build();

        if (this.existingId != null) {
            exhibition.setId(this.existingId);
        }

        return exhibition;
    }
}
