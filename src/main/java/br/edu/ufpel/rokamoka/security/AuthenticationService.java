package br.edu.ufpel.rokamoka.security;


import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * A service for handling user authentication.
 *
 * @author iyisakuma
 */
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Generates a JWT for the given authentication object.
     *
     * @param authentication An {@link Authentication} object containing the user's credentials.
     * @return A {@link String} containing the JWT.
     */
    public String authenticate(Authentication authentication) {
        return this.jwtService.generateToken(authentication);
    }

    /**
     * Performs a basic authentication and generates a JWT if it is successful.
     *
     * @param nome            The username to authenticate.
     * @param undecodedPasswd The password to authenticate.
     * @return A JWT if the authentication is successful, null otherwise.
     */
    public String basicAuthenticationAndGenerateJWT(String nome, String undecodedPasswd) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(nome, undecodedPasswd);
        Authentication authentication = this.authenticationManager.authenticate(authToken);
        return this.authenticate(authentication);
    }
}
