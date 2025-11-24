package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString(of = {"nome"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emblema")
public class Emblem extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String nome;

    @Column(length = 1000) private String descricao;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exposicao_id", nullable = false, unique = true)
    private Exhibition exhibition;

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
        Emblem other = (Emblem) o;
        if (this.id != null && Objects.equals(this.id, other.getId())) {
            return true;
        }
        boolean isSameName = this.nome != null && Objects.equals(this.nome, other.getNome());
        boolean isSameExhibition = this.exhibition != null && Objects.equals(this.exhibition, other.getExhibition());
        return isSameName && isSameExhibition;
    }
}
