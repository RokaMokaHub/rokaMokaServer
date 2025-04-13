package br.edu.ufpel.rokamoka.context;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author mauriciomucci
 */
@Getter
public class ApiResponse<T> {

    private final T body;
    private final int httpStatus;
    private final String exception;
    private final String exceptionMessage;

    public ApiResponse(ServiceContext ctx) {this(ctx, null);}

    public ApiResponse(ServiceContext ctx, T body) {
        this.body = body;

        HttpStatus status = ctx.getHttpStatus();
        this.httpStatus = status == null
                ? 0
                : status.value();

        Exception e = ctx.getException();
        if (e == null) {
            this.exception = this.exceptionMessage = "";
        } else {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            this.exception = cause.getClass().getSimpleName();
            this.exceptionMessage = cause.getMessage();
        }
    }
}
