package br.edu.ufpel.rokamoka.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author mauriciomucci
 */
@Getter
@Component
public class SecurityConfigProperties {

    @Value("${jwt.public.key}") private RSAPublicKey publicKey;
    @Value("${jwt.private.key}") private RSAPrivateKey privateKey;
}
