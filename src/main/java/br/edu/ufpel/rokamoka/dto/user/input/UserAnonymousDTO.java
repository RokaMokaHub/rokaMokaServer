package br.edu.ufpel.rokamoka.dto.user.input;


import jakarta.validation.constraints.Pattern;


public record UserAnonymousDTO(
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Apenas letras, números, hífen e underline são permitidos") String userName,
        String deviceId) {

}
