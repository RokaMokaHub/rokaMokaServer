package br.edu.ufpel.rokamoka.dto.user.input;

import br.edu.ufpel.rokamoka.utils.user.EmailConstraint;
import br.edu.ufpel.rokamoka.utils.user.PasswordConstraint;
import br.edu.ufpel.rokamoka.utils.user.UserNameConstraint;

/**
 * @author mauri
 */
public record UserResetPasswordDTO (
        @EmailConstraint String email,
        @UserNameConstraint String name,
        @PasswordConstraint String oldPassword,
        @PasswordConstraint String newPassword
){}
