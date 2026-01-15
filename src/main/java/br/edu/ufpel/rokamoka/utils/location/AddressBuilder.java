package br.edu.ufpel.rokamoka.utils.location;

import br.edu.ufpel.rokamoka.core.Address;
import br.edu.ufpel.rokamoka.dto.location.input.AddressInputDTO;

/**
 * A builder class responsible for constructing instances of {@code Address}.
 *
 * @author MauricioMucci
 * @see Address
 * @see AddressInputDTO
 */
public final class AddressBuilder {

    private Long existingId;
    private final AddressInputDTO input;

    public AddressBuilder(AddressInputDTO input) {
        this.input = input;
    }

    public AddressBuilder(Long existingId, AddressInputDTO input) {
        this.existingId = existingId;
        this.input = input;
    }

    public Address build() {
        Address address = Address.builder()
                .rua(this.input.rua())
                .numero(this.input.numero())
                .cep(this.input.cep())
                .complemento(this.input.complemento())
                .build();

        if (this.existingId != null) {
            address.setId(this.existingId);
        }

        return address;
    }
}
