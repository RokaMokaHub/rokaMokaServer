package br.edu.ufpel.rokamoka.dto.location.output;

import br.edu.ufpel.rokamoka.core.Location;

/**
 *
 * @author MauricioMucci
 */
public record LocationOutputDTO(Long id, String nome, AddressOutputDTO endereco) {

    public LocationOutputDTO(Location location) {
        this(location.getId(), location.getNome(), new AddressOutputDTO(location.getEndereco()));
    }
}
