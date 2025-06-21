package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.dto.address.input.EnderecoDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "endereco")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rua;
    private String numero;
    private String cep;
    private String complemento;

    public Address(EnderecoDTO enderecoDTO) {
        this.cep = enderecoDTO.cep();
        this.complemento = enderecoDTO.complemento();
        this.rua = enderecoDTO.rua();
        this.numero = enderecoDTO.numero();
    }
}
