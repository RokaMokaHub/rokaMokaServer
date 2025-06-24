package br.edu.ufpel.rokamoka.service.user;

import br.edu.ufpel.rokamoka.core.Role;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAuthDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import jakarta.validation.Valid;

/**
 * Service interface for managing and retrieving information related to {@link User}.
 *
 * @author MauricioMucci
 * @see UserService
 */
public interface IUserService {

    UserAuthDTO createNormalUser(@Valid UserBasicDTO userDTO) throws RokaMokaContentDuplicatedException;

    UserAnonymousResponseDTO createAnonymousUser(@Valid UserAnonymousRequestDTO userDTO)
            throws RokaMokaContentDuplicatedException;

    void resetUserPassword(@Valid UserResetPasswordDTO userDTO)
            throws RokaMokaContentNotFoundException, RokaMokaForbiddenException;

    UserAuthDTO createReseacher(UserBasicDTO userDTO) throws RokaMokaContentDuplicatedException;

    void updateRole(User requester, Role role);

    UserOutputDTO getLoggedUserInformation() throws RokaMokaContentNotFoundException;

    User findLoggedUser() throws RokaMokaContentNotFoundException;
}
