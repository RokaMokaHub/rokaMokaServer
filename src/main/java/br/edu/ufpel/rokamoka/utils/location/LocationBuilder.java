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

    private final LocationInputDTO input;
    private final Address address;

    public LocationBuilder(LocationInputDTO input, Address address) {
        this.input = input;
        this.address = address;
    }

    public Location build() {
        return Location.builder()
                .nome(this.input.name())
                .endereco(this.address)
                .build();
    }
}
