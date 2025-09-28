package br.edu.ufpel.rokamoka.dto.location.output;

import br.edu.ufpel.rokamoka.core.Address;

/**
 *
 * @author MauricioMucci
 */
public record AddressOutputDTO(Long id, String cep) {

    public AddressOutputDTO(Address address) {
        this(address.getId(), address.getCep());
    }
}
