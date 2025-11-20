package br.edu.ufpel.rokamoka.dto.authentication.input;

import br.edu.ufpel.rokamoka.utils.user.EmailConstraint;
import br.edu.ufpel.rokamoka.utils.user.PasswordConstraint;
import br.edu.ufpel.rokamoka.utils.user.UserNameConstraint;

/**
 * @author mauri
 */
public record AuthForgotPasswordDTO(
        @EmailConstraint String email,
        @UserNameConstraint String name,
        @PasswordConstraint String newPassword
){}
