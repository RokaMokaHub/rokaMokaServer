package br.edu.ufpel.rokamoka.dto.location.output;

import br.edu.ufpel.rokamoka.core.Location;

/**
 * A Data Transfer Object (DTO) representing the output data of a location.
 *
 * @param id The unique identifier of the location.
 * @param nome The unique name of the location.
 * @param endereco The address details of the location, encapsulated within an {@code AddressOutputDTO}.
 *
 * @author MauricioMucci
 * @see Location
 * @see AddressOutputDTO
 */
public record LocationOutputDTO(Long id, String nome, AddressOutputDTO endereco) {

    public LocationOutputDTO(Location location) {
        this(location.getId(), location.getNome(), new AddressOutputDTO(location.getEndereco()));
    }
}
