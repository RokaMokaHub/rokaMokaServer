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
    private final Location location;

    public LocationBuilder(Address address, LocationInputDTO input) {
        this(null, address, input);
    }

    public LocationBuilder(Location location, Address address, LocationInputDTO input) {
        this.location = location;
        this.input = input;
        this.address = address;
    }

    public Location build() {
        if (this.location != null) {
            throw new IllegalStateException("Location deve ser null ao criar");
        }
        return Location.builder()
                .nome(this.input.nome())
                .endereco(this.address)
                .build();
    }

    public Location update() {
        if (this.location == null) {
            throw new IllegalStateException("Location não pode ser null ao atualizar");
        }
        this.location.setNome(this.input.nome());
        this.location.setEndereco(this.address);
        return this.location;
    }
}
