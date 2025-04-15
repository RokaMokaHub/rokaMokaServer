package br.edu.ufpel.rokamoka.exceptions;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
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
public class GlobalExceptionHandler extends RokaMokaController {

    @ExceptionHandler(value = {RokaMokaContentDuplicated.class})
    public ResponseEntity<ApiResponseWrapper<Void>> handleBadRequest(Exception ex) {
        return error(ex, HttpStatus.BAD_REQUEST);
    }
}
