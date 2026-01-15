package br.edu.ufpel.rokamoka.dto.location.input;

import br.edu.ufpel.rokamoka.utils.location.CepConstraint;
import jakarta.validation.constraints.NotBlank;

/**
 * A Data Transfer Object (DTO) representing the input data of a location.
 *
 * @param rua The street name of the address
 * @param numero The house/building number
 * @param cep The postal code in format XXXXXXXX
 * @param complemento Additional address information (optional)
 *
 * @author MauricioMucci
 * @see CepConstraint
 */
public record AddressInputDTO(@NotBlank(message = "A rua é obrigatória") String rua,
                              @NotBlank(message = "O número é obrigatório") String numero,
                              @CepConstraint String cep,
                              String complemento) {}
