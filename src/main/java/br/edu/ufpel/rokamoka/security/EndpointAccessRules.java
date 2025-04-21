package br.edu.ufpel.rokamoka.security;


import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class EndpointAccessRules {

    private final String[] endpointsWithoutAuthentication =
            { "/user/login", "/user/anonymous/create", "/user/normal/create", "/error", "/swagger-ui/**",
                    "/v3/api-docs/**", "/swagger-resources/**" };

    private final String[] endpointsWithAuthentication = {
            "/teste/**"
    };
}
