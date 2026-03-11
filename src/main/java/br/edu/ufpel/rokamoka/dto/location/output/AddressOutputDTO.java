package br.edu.ufpel.rokamoka.dto.location.output;

import br.edu.ufpel.rokamoka.core.Address;

/**
 * A Data Transfer Object (DTO) representing the input data of a location.
 *
 * @param rua The street name of the address.
 * @param cep The postal code in format XXXXXXXX.
 * @param numero The number of the address.
 * @param complemento The complement of the address.
 *
 * @author MauricioMucci
 */
public record AddressOutputDTO(String rua, String cep, String numero, String complemento) {

    public AddressOutputDTO(Address address) {
        this(address.getRua(), address.getCep(), address.getNumero(), address.getComplemento());
    }
}
