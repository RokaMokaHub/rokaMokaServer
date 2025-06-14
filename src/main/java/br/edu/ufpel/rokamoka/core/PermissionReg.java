
package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "registro_solicitacao")
@Entity
public class PermissionReg {
    @Id
    private Long id;
    @OneToOne
    @JoinColumn(name = "revisor_id")
    private User reviewer;
    @OneToOne
    @JoinColumn(name = "solicitacao_id")
    private PermissionReq request;
    @Column(name = "justificativa")
    private String justification ;
}
