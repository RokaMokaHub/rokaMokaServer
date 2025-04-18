package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.*;
import lombok.*;


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
@Table(name = "role")
public class Role {

    public enum RoleName {
        ADMINISTRATOR, SEARCHER, CURATOR, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;
}
