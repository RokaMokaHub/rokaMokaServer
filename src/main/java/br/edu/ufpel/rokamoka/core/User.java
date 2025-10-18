package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * A user of the system.
 *
 * <p>This entity is used to represent a user of the system. The user can have
 * multiple roles and can be associated with multiple devices.
 *
 * @author iyisakuma
 */
@Getter
@Setter
@Builder
@ToString(of = {"email"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(nullable = false, unique = true) private String nome;

    @Column(name = "first_name") private String firstName;
    @Column(name = "last_name") private String lastName;

    @Column(nullable = false) private String senha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    private Role role;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User other = (User) o;
        if (this.id != null && Objects.equals(this.id, other.getId())) {
            return true;
        }
        return this.nome != null && Objects.equals(this.nome, other.getNome());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp ? hp.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : this.getClass().hashCode();
    }
}
