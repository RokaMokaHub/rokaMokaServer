package br.edu.ufpel.rokamoka.security;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EndpointAccessRules {

    private final String[] endpointsWithoutAuthentication = {
            "user/login",
            "user/register"
    };

    private final String[] endpointsWithAuthentication = {
            "teste/**"
    };
}
