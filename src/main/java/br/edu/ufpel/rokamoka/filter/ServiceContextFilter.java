package br.edu.ufpel.rokamoka.filter;


import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

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

/**
 * This filter is responsible for setting up the service context for each request and ensuring that authentication is
 * properly handled. It skips filtering for specific endpoints that do not require authentication.
 *
 * @author mauriciomucci
 * @see UserDetailsService
 * @see EndpointAccessRules
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceContextFilter implements Filter {

    private final UserDetailsService userDetailService;
    private final EndpointAccessRules endpointAccessRules;
    private final AntPathMatcher matcher = new AntPathMatcher();

    /**
     * Processes incoming requests and applies the filter logic. It sets up the service context, checks if the filter
     * should be skipped, and ensures that the user is authenticated before proceeding.
     *
     * @param request the incoming {@link ServletRequest}
     * @param response the outgoing {@link ServletResponse}
     * @param chain the {@link FilterChain} to pass the request and response to the next filter
     *
     * @throws IOException if an I/O error occurs during processing
     * @throws ServletException if a servlet-specific error occurs during processing
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        ServiceContext ctx = ServiceContext.newContext();

        if (shouldSkipFilter(request)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = this.userDetailService.loadUserByUsername(auth.getName());
        ctx.setUser((UserAuthenticated) userDetails);

        chain.doFilter(request, response);
    }

    /**
     * This method checks if the incoming request's path matches any of the endpoints that are configured to bypass
     * authentication. If a match is found, the filter will be skipped for that request.
     *
     * @param request the incoming {@link ServletRequest} to be evaluated
     *
     * @return true if the filter should be skipped, false otherwise
     */
    private boolean shouldSkipFilter(ServletRequest request) {
        String requestPath = new UrlPathHelper().getPathWithinApplication((HttpServletRequest) request);
        return Arrays.stream(this.endpointAccessRules.getEndpointsWithoutAuthentication())
                .anyMatch(endpointsPermited -> this.matcher.match(endpointsPermited, requestPath));
    }

}
