package br.edu.ufpel.rokamoka.context;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * A generic API response wrapper that encapsulates the response body, HTTP status, and exception details (if any) for a
 * given service context.
 *
 * @param <T> the type of the response body
 *
 * @author mauriciomucci
 */
@Getter
public class ApiResponseWrapper<T> {

    private final T body;
    private final int httpStatus;
    private final String exception;
    private final String exceptionMessage;

    public ApiResponseWrapper(ServiceContext ctx) {this(ctx, null);}

    public ApiResponseWrapper(ServiceContext ctx, T body) {
        this.body = body;

        HttpStatus status = ctx.getHttpStatus();
        this.httpStatus = status == null
                ? 0
                : status.value();

        Exception e = ctx.getException();
        if (e == null) {
            this.exception = this.exceptionMessage = "";
        } else {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            this.exception = cause.getClass().getSimpleName();
            this.exceptionMessage = cause.getMessage();
        }
    }
}
