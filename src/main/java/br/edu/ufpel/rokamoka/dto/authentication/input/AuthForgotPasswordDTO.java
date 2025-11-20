package br.edu.ufpel.rokamoka.dto.authentication.input;

import br.edu.ufpel.rokamoka.utils.user.EmailConstraint;
import br.edu.ufpel.rokamoka.utils.user.PasswordConstraint;
import br.edu.ufpel.rokamoka.utils.user.UserNameConstraint;

/**
 * A Data Transfer Object (DTO) for handling user password recovery requests.
 *
 * @param email The user's email address.
 * @param name The user's username.
 * @param newPassword The user's new password.
 *
 * @author MauricioMucci
 * @see AuthForgotPasswordDTO
 * @see EmailConstraint
 * @see UserNameConstraint
 * @see PasswordConstraint
 */
public record AuthForgotPasswordDTO(
        @EmailConstraint String email, @UserNameConstraint String name, @PasswordConstraint String newPassword) {}
