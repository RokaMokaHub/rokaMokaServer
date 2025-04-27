package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * A role of a user.
 *
 * @author iyiSakuma
 */
@Getter
@Setter
@Builder
@ToString(of = {"name"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfil")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleEnum name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "perfil_acao",
            joinColumns = @JoinColumn(
                    name = "perfil_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_perfil")),
            inverseJoinColumns = @JoinColumn(
                    name = "acao_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_acao"))
    )
    private Set<Action> actions;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Role role = (Role) o;
        return this.id != null && Objects.equals(this.id, role.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
