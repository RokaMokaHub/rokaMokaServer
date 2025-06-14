package br.edu.ufpel.rokamoka.security;


import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EndpointAccessRules {

    private final String[] endpointsWithoutAuthentication = {
            "/user/login", "/user/anonymous/create", "/user/normal/create", "/error", "/swagger-ui/**",
            "/v3/api-docs/**", "/swagger-resources/**", "/researcher/create"
    };

    private final String[] exhibitionEndpoints = {"/exhibition/**"};

    private final String[] permissionsEndpoints = {"/evaluation/permission/**"};
}
