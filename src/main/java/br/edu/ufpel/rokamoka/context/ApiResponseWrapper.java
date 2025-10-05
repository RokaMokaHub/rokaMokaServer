package br.edu.ufpel.rokamoka.context;

import br.edu.ufpel.rokamoka.utils.exception.ExceptionDetails;
import br.edu.ufpel.rokamoka.utils.exception.MethodArgumentNotValidExceptionHelper;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

/**
 * A generic wrapper for API responses, encapsulating response body, HTTP status and exception details.
 *
 * <p>This provides a standardized structure for handling API responses, including error propagation.
 *
 * <p>This class is immutable and is used to standardize the structure of responses in a service-oriented context.</p>
 *
 * @param <T> The type of the response body.
 *
 * @author mauriciomucci
 * @see Getter
 */
@Getter
public class ApiResponseWrapper<T> {

    private static final int DEFAULT_HTTP_STATUS = 0;
    private static final String EMPTY_STRING = "";

    private final T body;
    private final int httpStatus;
    private final String exception;
    private final String exceptionMessage;

    public ApiResponseWrapper(ServiceContext ctx) {
        this(ctx, null);
    }

    public ApiResponseWrapper(ServiceContext ctx, T body) {
        this.body = body;
        this.httpStatus = Optional.ofNullable(ctx.getHttpStatus())
                .map(HttpStatus::value)
                .orElse(DEFAULT_HTTP_STATUS);

        ExceptionDetails exceptionDetails = this.extractExceptionDetails(ctx.getException());
        this.exception = exceptionDetails.name();
        this.exceptionMessage = exceptionDetails.message();
    }

    private ExceptionDetails extractExceptionDetails(Exception exception) {
        if (exception == null) {
            return new ExceptionDetails(EMPTY_STRING, EMPTY_STRING);
        }

        if (exception instanceof MethodArgumentNotValidException validationException) {
            return new MethodArgumentNotValidExceptionHelper(validationException).buildExceptionDetails();
        }

        Throwable rootCause = this.getRootCause(exception);
        return new ExceptionDetails(rootCause.getClass().getSimpleName(), rootCause.getMessage());
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }
}
