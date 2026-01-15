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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exposicao")
public class Exhibition extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false) private String name;
    @Column(name = "descricao") private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "local_id", nullable = false)
    private Location location;

    @Default
    @Transient
    private List<Artwork> artworks = new ArrayList<>();

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
        Exhibition other = (Exhibition) o;
        if (this.id != null && Objects.equals(this.id, other.getId())) {
            return true;
        }
        boolean isSameName = this.name != null && Objects.equals(this.name, other.getName());
        boolean isSameLocation = this.location != null && Objects.equals(this.location, other.getLocation());
        return isSameName && isSameLocation;
    }
}
