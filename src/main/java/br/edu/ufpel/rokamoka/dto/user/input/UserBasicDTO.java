package br.edu.ufpel.rokamoka.dto.user.input;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * A Data Transfer Object for basic user information.
 * <p>
 * This record is used to capture the basic information required for creating or authenticating a user.
 * </p>
 *
 * @param email    The email of the user. Must be a valid email format and not null.
 * @param password The user's password. Must be between 8 and 20 characters, inclusive, and not blank.
 * @param name     The name of the user. Must not be blank.
 * @param deviceId The ID of the user's device.
 * @Author: iyisakuma
 */
public record UserBasicDTO(
        @NotNull @Email String email, @NotBlank @Size(min = 8, max = 20) String password,
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Apenas letras, números, hífen e underline são permitidos") String name,
        String deviceId
) {}