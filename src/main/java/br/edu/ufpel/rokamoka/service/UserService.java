package br.edu.ufpel.rokamoka.service;


import org.springframework.validation.annotation.Validated;

import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;

import jakarta.validation.Valid;

@Validated
public interface UserService {

    UserResponseDTO createNormalUser(@Valid UserBasicDTO user) throws RokaMokaContentDuplicatedException;

    UserAnonymousResponseDTO createAnonymousUser(UserAnonymousDTO userDTO) throws RokaMokaContentDuplicatedException;
}
