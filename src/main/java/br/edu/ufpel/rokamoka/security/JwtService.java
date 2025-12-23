package br.edu.ufpel.rokamoka.security;

import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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

    private static final String ROKAMOKA = "ROKAMOKA";

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    /**
     * Retrieves the subject (e.g., username or user ID) from the given JWT token after validating it.
     *
     * @param token the JWT token as a {@link String}, which must be a valid, non-expired token.
     *
     * @return the subject of the token as a {@link String}, which typically identifies the user.
     * @throws RokaMokaForbiddenException if the token is expired or does not contain a valid expiration claim.
     */
    public String getSubject(String token) {
        Jwt jwt = this.jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    /**
     * Generates a JSON Web Token (JWT) for the specified authenticated user.
     *
     * <p>This method creates a JWT with the following claims:
     * <ul>
     *   <li>Issuer: "ROKAMOKA"</li>
     *   <li>Issued at: the current time</li>
     *   <li>Expiration time: 15 minutes from the issued time</li>
     *   <li>Subject: the username of the authenticated user</li>
     * </ul>
     *
     * @param user the {@link UserAuthenticated} object representing the authenticated user.
     *
     * @return a signed JWT as a {@link String}, which can be used for authentication and authorization.
     */
    public String generateToken(UserAuthenticated user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ROKAMOKA)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(900L)) // 15 minutes
                .subject(user.getUsername())
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

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
     *
     * @return the generated token
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ROKAMOKA)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(36000L)) // 10 hours
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
