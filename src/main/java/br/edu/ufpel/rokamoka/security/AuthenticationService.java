package br.edu.ufpel.rokamoka.security;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private JwtService jwtService;
    
    public String authenticate(Authentication authentication) {
        return jwtService.generateToken(authentication);
    }
}
