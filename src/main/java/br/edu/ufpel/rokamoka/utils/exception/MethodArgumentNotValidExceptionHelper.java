package br.edu.ufpel.rokamoka.utils.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

/**
 *
 * @author MauricioMucci
 */
public record MethodArgumentNotValidExceptionHelper(MethodArgumentNotValidException exception) {

    public ExceptionDetails buildExceptionDetails() {
        String message = this.exception.getBindingResult().getFieldErrors().stream().map(error -> {
            String fieldName = error.getField();
            String defaultMessage = error.getDefaultMessage();
            return String.format("%s: %s", fieldName, defaultMessage);
        }).collect(Collectors.joining("; "));

        if (message.isEmpty()) {
            message = "Erro de validação nos dados fornecidos";
        }

        return new ExceptionDetails(this.exception.getClass().getSimpleName(), message);
    }
}
