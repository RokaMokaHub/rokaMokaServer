package br.edu.ufpel.rokamoka.service.user;

import br.edu.ufpel.rokamoka.core.Role;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAuthDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserOutputDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for managing and retrieving information related to {@link User}.
 *
 * @author MauricioMucci
 * @see UserService
 */
@Validated
public interface IUserService {

    UserAuthDTO createNormalUser(@Valid UserBasicDTO userDTO);

    UserAnonymousResponseDTO createAnonymousUser(@Valid UserAnonymousRequestDTO userDTO);

    void resetUserPassword(@Valid UserResetPasswordDTO userDTO);

    UserAuthDTO createResearcher(@Valid UserBasicDTO userDTO);

    void updateRole(@NotNull User requester, Role role);

    UserOutputDTO getLoggedUserInformation();

    User getByNome(@NotNull String nome);

    User getLoggedUser();
}
