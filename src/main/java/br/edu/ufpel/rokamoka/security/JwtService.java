package br.edu.ufpel.rokamoka.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * A service for generating JSON Web Tokens (JWTs).
 *
 * @author iyisakuma
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    /**
     * Generates a JWT token given an {@link Authentication} object.
     *
     * <p>The generated token will have the following claims:
     * <ul>
     * <li>Issuer: "spring-security-jwt"</li>
     * <li>Issued at: the current time</li>
     * <li>Expiration time: the current time plus 10 hours</li>
     * <li>Subject: the username of the user</li>
     * <li>Scope: a space-separated list of the user's roles</li>
     * </ul>
     *
     * @param authentication the authentication object to generate the token for
     * @return the generated token
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000000L; // 10 hours in milliseconds

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("spring-security-jwt")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
