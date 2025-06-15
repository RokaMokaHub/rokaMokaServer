package br.edu.ufpel.rokamoka.dto.user.output;

import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokaDexOutputDTO;

/**
 * @author mauri
 */
public record UserOutputDTO(
        String name,
        String email,
        String role,
        MokaDexOutputDTO mokaDex
) {

    public UserOutputDTO(User loggedUser) {
        this(
                loggedUser.getNome(),
                loggedUser.getEmail(),
                loggedUser.getRole().getName().getDescription(),
                null
        );
    }
}
