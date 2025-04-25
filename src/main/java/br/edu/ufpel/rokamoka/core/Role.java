package br.edu.ufpel.rokamoka.core;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


/**
 * A role of a user.
 *
 * @author iyiSakuma
 */
@Getter
@Setter
@Builder
@ToString(of = {"name"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfil")
public class Role {

    public enum RoleName {
        ADMINISTRATOR, SEARCHER, CURATOR, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "perfil_acao",
            joinColumns
                    = @JoinColumn(name = "perfil_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_perfil")),
            inverseJoinColumns
                    = @JoinColumn(name = "acao_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_acao")))
    private Set<Action> actions;
}
