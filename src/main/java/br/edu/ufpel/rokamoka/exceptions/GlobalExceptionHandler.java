package br.edu.ufpel.rokamoka.exceptions;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles global exceptions for the application by providing appropriate responses for various HTTP error scenarios.
 *
 * <p>This controller provides centralized exception handling for all controllers by responding with a consistent
 * structure using {@link ApiResponseWrapper}.
 *
 * @author mauriciomucci
 * @see RokaMokaController
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends RokaMokaController {

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiResponseWrapper<Void>> handleBadRequest(Exception ex) {
        return this.error(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            RokaMokaContentDuplicatedException.class,
    })
    public ResponseEntity<ApiResponseWrapper<Void>> handleConflict(Exception ex) {
        return this.error(ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {
            RokaMokaContentNotFoundException.class
    })
    public ResponseEntity<ApiResponseWrapper<Void>> handleNotFound(Exception ex) {
        return this.error(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            RokaMokaForbiddenException.class
    })
    public ResponseEntity<ApiResponseWrapper<Void>> handleForbidden(Exception ex) {
        return this.error(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {
            ServiceException.class
    })
    public ResponseEntity<ApiResponseWrapper<Void>> handleInternalServerError(Exception ex) {
        return this.error(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
