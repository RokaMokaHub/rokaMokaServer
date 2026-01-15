package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

/**
 * A role of a user.
 *
 * @author iyiSakuma
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfil")
public class Role extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleEnum name;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hp
               ? hp.getHibernateLazyInitializer().getPersistentClass().hashCode()
               : this.getClass().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Role other = (Role) o;
        if (this.id != null && Objects.equals(this.id, other.getId())) {
            return true;
        }
        return this.name != null && Objects.equals(this.name, other.getName());
    }

    @Override
    public String toString() {
        return this.name.getDescription();
    }
}
