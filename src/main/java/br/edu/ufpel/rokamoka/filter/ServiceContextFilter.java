package br.edu.ufpel.rokamoka.filter;

import br.edu.ufpel.rokamoka.config.SecurityConfig;
import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.security.EndpointAccessRules;
import br.edu.ufpel.rokamoka.security.UserAuthenticated;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;

/**
 * @author mauriciomucci
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceContextFilter implements Filter {

    private final EndpointAccessRules endpointAccessRules;
    private final UserDetailsService userDetailService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ServiceContext ctx = ServiceContext.newContext();

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestPath = new UrlPathHelper().getPathWithinApplication(httpRequest).substring(1);

        for (String endpoint : endpointAccessRules.getEndpointsWithoutAuthentication()) {
            if (requestPath.startsWith(endpoint)) {
                chain.doFilter(request, response);
                return;
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(auth.getName());
        ctx.setUser((UserAuthenticated) userDetails);

        chain.doFilter(request, response);
    }
}
