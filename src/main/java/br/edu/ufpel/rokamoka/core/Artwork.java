package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString(of = {"nome", "nomeArtista"})
@RequiredArgsConstructor
@Entity
@Table(name = "obra")
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    private String nomeArtista;
    private String descricao;
    private String link;
    private String qrCode;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exposicao_id")
    private Exhibition exhibition;
    @OneToMany
    @JoinColumn(name = "obra_id")
    private Set<Image> images;

    public Artwork(ArtworkInputDTO dto, Exhibition exhibition) {
        this.nome = dto.nome();
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
