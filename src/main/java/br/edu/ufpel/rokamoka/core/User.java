package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
import java.util.Set;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    @Column(nullable = false) private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_perfil",
            joinColumns = @JoinColumn(name = "usario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario")),
            inverseJoinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_perfil"))
    )
    private Set<Role> roles;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return this.id != null && Objects.equals(this.id, user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
