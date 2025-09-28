package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.dto.location.input.AddressInputDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

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

    @Column(nullable = false) private String rua;

    @Column(nullable = false) private String numero;

    @Column(nullable = false) private String cep;

    private String complemento;

    public Address(AddressInputDTO addressInputDTO) {
        this.cep = addressInputDTO.cep();
        this.complemento = addressInputDTO.complemento();
        this.rua = addressInputDTO.rua();
        this.numero = addressInputDTO.numero();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Address other = (Address) o;
        if (this.id != null && Objects.equals(this.id, other.getId())) {
            return true;
        }
        return this.rua != null && this.numero != null && this.cep != null &&
               Objects.equals(this.rua, other.rua) &&
               Objects.equals(this.numero, other.numero) &&
               Objects.equals(this.cep, other.cep);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
                .getPersistentClass()
                .hashCode() : getClass().hashCode();
    }
}
