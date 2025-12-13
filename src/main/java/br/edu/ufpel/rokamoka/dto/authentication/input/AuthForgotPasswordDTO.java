package br.edu.ufpel.rokamoka.dto.authentication.input;

import br.edu.ufpel.rokamoka.utils.user.PasswordConstraint;
import jakarta.validation.constraints.NotBlank;

/**
 * A Data Transfer Object (DTO) for handling user password recovery requests.
 *
 * @param token The user's token for password recovery.
 * @param newPassword The user's new password.
 *
 * @author MauricioMucci
 * @see AuthForgotPasswordDTO
 * @see PasswordConstraint
 */
public record AuthForgotPasswordDTO(@NotBlank String token, @PasswordConstraint String newPassword) {}
