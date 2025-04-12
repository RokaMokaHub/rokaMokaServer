package br.edu.ufpel.rokamoka.security;

import br.edu.ufpel.rokamoka.core.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class UserAuthenticated implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getSenha();
    }

    @Override
    public String getUsername() {
        return user.getNome();
    }
}
