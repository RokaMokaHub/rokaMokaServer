package br.edu.ufpel.rokamoka.security;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }

    public String basicAuthenticationAndGenerateJWT(String nome, String undecodedPasswd) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(nome, undecodedPasswd);
        Authentication authentication = this.authenticationManager.authenticate(authToken);
        return this.authenticate(authentication);
    }
}
