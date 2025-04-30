package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "obra")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String nome;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exposicao_id")
    private Exhibition exhibition;

    public Artwork(ArtworkInputDTO dto, Exhibition exhibition) {
        this.nome = dto.nome();
        ;
        this.exhibition = exhibition;
    }
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Artwork artwork = (Artwork) o;
        return this.id != null && Objects.equals(this.id, artwork.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
