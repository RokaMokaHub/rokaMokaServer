package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.dto.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicated;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {

    UserResponseDTO createNormalUser(@Valid UserBasicDTO user) throws RokaMokaContentDuplicated;
}
