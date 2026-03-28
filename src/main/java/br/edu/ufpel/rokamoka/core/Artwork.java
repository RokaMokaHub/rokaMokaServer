package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.core.audit.Auditable;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString(of = {"nome", "nomeArtista"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "obra", uniqueConstraints = @UniqueConstraint(columnNames = {"qr_code"}))
public class Artwork extends Auditable {

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

    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "obra_id")
    private Set<Image> images = new HashSet<>();

    public Artwork(ArtworkInputDTO dto, Exhibition exhibition) {
        this.nome = dto.nome();
        this.exhibition = exhibition;
    }

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
        var other = (Artwork) o;
        if (this.id != null && Objects.equals(this.id, other.getId())) {
            return true;
        }
        return this.qrCode != null && Objects.equals(this.qrCode, other.getQrCode());
    }
}
