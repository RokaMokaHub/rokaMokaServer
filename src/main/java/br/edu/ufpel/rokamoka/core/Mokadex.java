package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.core.audit.Auditable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString(of = {"id", "usuario"})
@Entity
@Table(name = "mokadex")
public class Mokadex extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER) private User usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mokadex_emblema", joinColumns = @JoinColumn(name = "mokadex_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_mokadex")),
            inverseJoinColumns = @JoinColumn(name = "emblema_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_emblema")))
    private Set<Emblem> emblems = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "mokadex_obra", joinColumns = @JoinColumn(name = "mokadex_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_mokadex")),
            inverseJoinColumns = @JoinColumn(name = "obra_id", referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_obra")))
    private Set<Artwork> artworks = new HashSet<>();

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
        Mokadex other = (Mokadex) o;
        return this.id != null && Objects.equals(this.id, other.getId());
    }

    public boolean containsArtwork(Artwork artwork) {
        return this.artworks.contains(artwork);
    }

    public boolean addArtwork(Artwork artwork) {
        if (artwork == null || this.artworks == null) {
            return false;
        }
        return this.artworks.add(artwork);
    }

    public boolean containsEmblem(Emblem emblem) {
        return this.emblems.contains(emblem);
    }

    public boolean addEmblem(Emblem emblem) {
        if (emblem == null || this.emblems == null) {
            return false;
        }
        return this.emblems.add(emblem);
    }
}
