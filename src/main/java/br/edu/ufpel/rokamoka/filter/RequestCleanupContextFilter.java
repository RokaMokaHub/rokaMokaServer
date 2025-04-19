package br.edu.ufpel.rokamoka.filter;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A filter implementation that cleans up the {@link ServiceContext} after processing each request.
 *
 * @author mauriciomucci
 * @see Slf4j
 * @see Component
 */
@Slf4j
@Component
public class RequestCleanupContextFilter implements Filter {

    /**
     * This method serves as a standard implementation of the {@link Filter#doFilter} method, intended to manage the
     * lifecycle of the {@link ServiceContext} for each request. It ensures that after the request is processed, any
     * context-specific data stored in {@link ServiceContext} is removed to prevent memory leaks or unintended data
     * sharing across threads.
     *
     * @param request the incoming {@link ServletRequest} that contains information about the request
     * @param response the outgoing {@link ServletResponse} where the response will be written
     * @param chain the {@link FilterChain} to delegate processing to the next filter in the chain
     *
     * @throws IOException if an I/O error occurs during processing the request
     * @throws ServletException if a servlet-specific error occurs during processing
     * @see ServiceContext#clearContext()
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            ServiceContext.clearContext();
        }
    }
}
