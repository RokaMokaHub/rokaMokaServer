package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Builder
@ToString(of = {"statu", "role"})
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
        Action action = (Action) o;
        return this.id != null && Objects.equals(this.id, action.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
