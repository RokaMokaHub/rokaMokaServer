package br.edu.ufpel.rokamoka.dto.user.input;

import br.edu.ufpel.rokamoka.utils.user.EmailConstraint;
import br.edu.ufpel.rokamoka.utils.user.PasswordConstraint;
import br.edu.ufpel.rokamoka.utils.user.UserNameConstraint;

/**
 * A Data Transfer Object for basic user information.
 * <p>
 * This record is used to capture the basic information required for creating or authenticating a user.
 * </p>
 *
 * @param email The email of the user. Must be a valid email format and not null.
 * @param password The user's password. Must be between 8 and 20 characters, inclusive, and not blank.
 * @param name The name of the user. Must not be blank.
 * @param deviceId The ID of the user's device.
 *
 * @author iyisakuma
 * @see EmailConstraint
 * @see PasswordConstraint
 * @see UserNameConstraint
 */
public record UserBasicDTO(
        @EmailConstraint String email,
        @PasswordConstraint String password,
        @UserNameConstraint String name,
        String deviceId
) {}
