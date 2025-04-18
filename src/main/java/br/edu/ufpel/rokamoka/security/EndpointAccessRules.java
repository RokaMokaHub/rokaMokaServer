package br.edu.ufpel.rokamoka.security;


import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class EndpointAccessRules {

    private final String[] endpointsWithoutAuthentication =
            { "/user/login", "/user/anonymous/create", "/user/normal/create"
    };

    private final String[] endpointsWithAuthentication = {
            "/teste/**"
    };
}
