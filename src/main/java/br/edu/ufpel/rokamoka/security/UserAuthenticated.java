package br.edu.ufpel.rokamoka.security;


import br.edu.ufpel.rokamoka.core.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * A {@link UserDetails} implementation for {@link User} objects.
 *
 * <p>This class is used to represent a user in the security context of the application.
 * It provides a way to retrieve the authorities granted to the user and to validate
 * a user's credentials.
 *
 * @author iyisakuma
 */
@RequiredArgsConstructor
public class UserAuthenticated implements UserDetails {

    private final User user;

    /**
     * Retrieves the authorities granted to the user.
     *
     * <p>This method returns a collection of {@link GrantedAuthority} based on the roles
     * associated with the user. Each role is mapped to a {@link SimpleGrantedAuthority}.
     *
     * @return A collection of authorities granted to the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles()
                .stream().flatMap(role -> role.getActions().stream())
                .map(action -> new SimpleGrantedAuthority(action.getName()))
                .toList();
    }

    /**
     * Returns the password of the user.
     *
     * <p>This method returns the password stored in the database for the
     * user represented by this object.
     *
     * @return The encoded password of the user.
     */
    @Override
    public String getPassword() {
        return user.getSenha();
    }

    /**
     * Retrieves the username of the user.
     *
     * <p>This method returns the username associated with the user. The username is used
     * to identify the user in the application.
     *
     * @return The username of the user.
     */
    @Override
    public String getUsername() {
        return user.getNome();
    }
}
