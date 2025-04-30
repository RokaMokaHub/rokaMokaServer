package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "mokadex")
public class Mokadex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mokadex_obra",
            joinColumns = @JoinColumn(
                    name = "mokadex_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_mokadex")),
            inverseJoinColumns = @JoinColumn(
                    name = "obra_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_obra"))
    )
    private Set<Artwork> artworks;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Mokadex mokadex = (Mokadex) o;
        return this.id != null && Objects.equals(this.id, mokadex.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
