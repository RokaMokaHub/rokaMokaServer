package br.edu.ufpel.rokamoka.core;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private Boolean ativo;

    @Column(nullable = false) private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();
}
