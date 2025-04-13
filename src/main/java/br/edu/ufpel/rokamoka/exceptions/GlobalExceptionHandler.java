package br.edu.ufpel.rokamoka.exceptions;

import br.edu.ufpel.rokamoka.context.ApiResponse;
import br.edu.ufpel.rokamoka.wrapper.ApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author mauriciomucci
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ApiResponseWrapper {

    @ExceptionHandler(value = {RokaMokaContentDuplicated.class})
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(Exception ex) {
        return error(ex, HttpStatus.BAD_REQUEST);
    }
}
