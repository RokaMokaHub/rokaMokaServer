package br.edu.ufpel.rokamoka.dto.location.output;

import br.edu.ufpel.rokamoka.core.Address;

/**
 * A Data Transfer Object (DTO) representing the input data of a location.
 *
 * @param id The unique identifier of the address.
 * @param rua The street name of the address.
 * @param cep The postal code in format XXXXXXXX.
 *
 * @author MauricioMucci
 */
public record AddressOutputDTO(Long id, String rua, String cep) {

    public AddressOutputDTO(Address address) {
        this(address.getId(), address.getRua(), address.getCep());
    }
}
