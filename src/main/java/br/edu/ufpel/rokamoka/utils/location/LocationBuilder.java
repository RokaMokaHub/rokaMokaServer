package br.edu.ufpel.rokamoka.utils.location;

import br.edu.ufpel.rokamoka.core.Address;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.location.input.LocationInputDTO;

/**
 * A builder class responsible for constructing instances of {@code Location}.
 *
 * @author MauricioMucci
 * @see Location
 * @see Address
 * @see LocationInputDTO
 */
public final class LocationBuilder {

    private Long existingId;
    private final LocationInputDTO input;
    private final Address address;

    public LocationBuilder(Long existingId, LocationInputDTO input, Address address) {
        this.existingId = existingId;
        this.input = input;
        this.address = address;
    }

    public LocationBuilder(LocationInputDTO input, Address address) {
        this.input = input;
        this.address = address;
    }

    public Location build() {
        Location location = Location.builder()
                .nome(this.input.nome())
                .endereco(this.address)
                .build();

        if (this.existingId != null) {
            location.setId(this.existingId);
        }

        return location;
    }
}
