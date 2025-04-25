package br.edu.ufpel.rokamoka.core;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * A user of the system.
 *
 * <p>This entity is used to represent a user of the system. The user can have
 * multiple roles and can be associated with multiple devices.
 *
 * @author iyisakuma
 */
@Getter
@Setter
@Builder
@ToString(of = {"email"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    @Column(nullable = false) private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_perfil",
            joinColumns = @JoinColumn(name = "usario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_usuario")),
            inverseJoinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_perfil"))
    )
    private Set<Role> roles;
}
