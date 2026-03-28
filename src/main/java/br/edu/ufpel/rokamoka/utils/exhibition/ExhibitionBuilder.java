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

    private final ExhibitionInputDTO input;
    private final Location location;
    private final Exhibition exhibition;

    public ExhibitionBuilder(Location location, ExhibitionInputDTO input) {
        this(null, location, input);
    }

    public ExhibitionBuilder(Exhibition exhibition, Location location, ExhibitionInputDTO input) {
        this.exhibition = exhibition;
        this.location = location;
        this.input = input;
    }

    public Exhibition build() {
        if (this.exhibition != null) {
            throw new IllegalStateException("Exhibition deve ser null ao criar");
        }
        return Exhibition.builder()
                .name(this.input.name())
                .description(this.input.description())
                .location(this.location)
                .build();
    }

    public Exhibition update() {
        if (this.exhibition == null) {
            throw new IllegalStateException("Exhibition não pode ser null ao atualizar");
        }
        this.exhibition.setName(this.input.name());
        this.exhibition.setDescription(this.input.description());
        this.exhibition.setLocation(this.location);
        return this.exhibition;
    }
}
