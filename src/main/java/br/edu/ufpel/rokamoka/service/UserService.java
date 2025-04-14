package br.edu.ufpel.rokamoka.service;


import org.springframework.validation.annotation.Validated;

import br.edu.ufpel.rokamoka.dto.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;

import jakarta.validation.Valid;

@Validated
public interface UserService {

    UserResponseDTO createNormalUser(@Valid UserBasicDTO user) throws RokaMokaContentDuplicatedException;
}
