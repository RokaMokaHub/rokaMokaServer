package br.edu.ufpel.rokamoka.dto.user.output;

import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;

/**
 * @author mauri
 */
public record UserOutputDTO(
        String name,
        String email,
        String role,
        MokadexOutputDTO mokaDex
) {

    public UserOutputDTO(User loggedUser) {
        this(
                loggedUser.getNome(),
                loggedUser.getEmail(),
                loggedUser.getRole().getName().getDescription(),
                null
        );
    }

    public UserOutputDTO(User loggedUser, MokadexOutputDTO mokadex) {
        this(
                loggedUser.getNome(),
                loggedUser.getEmail(),
                loggedUser.getRole().getName().getDescription(),
                mokadex
        );
    }
}
