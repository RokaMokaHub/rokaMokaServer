package br.edu.ufpel.rokamoka.core;

import br.edu.ufpel.rokamoka.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registro_solicitacao")
@Entity
public class PermissionReg extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "revisor_id")
    private User reviewer;
    @OneToOne
    @JoinColumn(name = "solicitacao_id")
    private PermissionReq request;
    @Column(name = "justificativa") private String justification;
}
