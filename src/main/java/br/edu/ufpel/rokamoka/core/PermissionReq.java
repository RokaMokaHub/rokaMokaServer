package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

@Getter
@Setter
@Builder
@ToString(of = {"status", "targetRole"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "solicitacao")
@Entity
public class PermissionReq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "solicitante_id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_objetivo_id")
    private Role targetRole;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        PermissionReq pq = (PermissionReq) o;
        return this.id != null && Objects.equals(this.id, pq.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    public boolean isPending() {
        return RequestStatus.PENDING.equals(this.status);
    }
}
