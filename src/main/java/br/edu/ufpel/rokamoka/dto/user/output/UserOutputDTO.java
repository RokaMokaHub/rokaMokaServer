package br.edu.ufpel.rokamoka.dto.user.output;

import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;

/**
 * A Data Transfer Object representing the output data of a user in the system.
 *
 * <ul>
 *   <li>Encapsulates key user details: name, email, and role.</li>
 *   <li>Optionally includes detailed information regarding the user's Mokadex collection.</li>
 * </ul>
 *
 * @param name The name of the user.
 * @param email The email of the user.
 * @param role The role of the user - describes permissions and responsibilities.
 * @param mokaDex Additional information about the user's Mokadex collection, or {@code null} if not provided.
 *
 * @author mauri
 * @see User
 * @see MokadexOutputDTO
 */
public record UserOutputDTO(
        String name,
        String firstName,
        String lastName,
        String email,
        String role,
        MokadexOutputDTO mokaDex
) {

    public UserOutputDTO(User loggedUser, MokadexOutputDTO mokadex) {
        this(
                loggedUser.getNome(),
                loggedUser.getFirstName(),
                loggedUser.getLastName(),
                loggedUser.getEmail(),
                loggedUser.getRole().getName().getDescription(),
                mokadex
        );
    }
}
