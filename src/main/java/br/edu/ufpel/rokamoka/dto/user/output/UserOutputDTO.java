package br.edu.ufpel.rokamoka.dto.user.output;

import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokaDexOutputDTO;

import java.util.Set;

/**
 * @author mauri
 */
public record UserOutputDTO(
        String name,
        String email,
        Set<RoleEnum> roleSet,
        MokaDexOutputDTO mokaDex
) {

    public UserOutputDTO(User loggedUser) {
        this(
                loggedUser.getNome(),
                loggedUser.getEmail(),
                loggedUser.getRolesEnum(),
                null
        );
    }
}
