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

    private final AddressInputDTO input;
    private final Address address;

    public AddressBuilder(AddressInputDTO input) {
        this(null, input);
    }

    public AddressBuilder(Address address, AddressInputDTO input) {
        this.address = address;
        this.input = input;
    }

    public Address build() {
        if (this.address != null) {
            throw new IllegalStateException("Address deve ser null ao criar");
        }
        return Address.builder()
                .rua(this.input.rua())
                .numero(this.input.numero())
                .cep(this.input.cep())
                .complemento(this.input.complemento())
                .build();
    }

    public Address update() {
        if (this.address == null) {
            throw new IllegalStateException("Address não pode ser null ao atualizar");
        }
        this.address.setRua(this.input.rua());
        this.address.setNumero(this.input.numero());
        this.address.setCep(this.input.cep());
        this.address.setComplemento(this.input.complemento());
        return this.address;
    }
}
