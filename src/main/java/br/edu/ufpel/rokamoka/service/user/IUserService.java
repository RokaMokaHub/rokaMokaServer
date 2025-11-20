package br.edu.ufpel.rokamoka.service.user;

import br.edu.ufpel.rokamoka.core.Role;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.authentication.input.AuthForgotPasswordDTO;
import br.edu.ufpel.rokamoka.dto.authentication.input.AuthResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.authentication.output.AuthOutputDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserInputDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
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

    AuthOutputDTO createNormalUser(@Valid UserInputDTO userDTO);

    UserAnonymousResponseDTO createAnonymousUser(@Valid UserAnonymousRequestDTO userDTO);

    AuthOutputDTO createResearcher(@Valid UserInputDTO userDTO);

    void resetUserPassword(@Valid AuthResetPasswordDTO userDTO);

    void forgotUserPassword(@Valid AuthForgotPasswordDTO forgotPasswordDTO);

    void updateRole(@NotNull User requester, Role role);

    UserOutputDTO getLoggedUserInformation();

    User getByNome(@NotNull String nome);

    User getLoggedUserOrElseThrow();
}
