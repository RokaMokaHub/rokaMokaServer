package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {

    UserResponseDTO createNormalUser(@Valid UserBasicDTO userDTO) throws RokaMokaContentDuplicatedException;

    UserAnonymousResponseDTO createAnonymousUser(@Valid UserAnonymousDTO userDTO)
            throws RokaMokaContentDuplicatedException;

    void resetUserPassword(@Valid UserBasicDTO userDTO)
            throws RokaMokaContentNotFoundException, RokaMokaForbiddenException;
}
