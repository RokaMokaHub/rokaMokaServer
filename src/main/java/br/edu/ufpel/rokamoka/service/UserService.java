package br.edu.ufpel.rokamoka.service;


import br.edu.ufpel.rokamoka.dto.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicated;


public interface UserService {

    UserResponseDTO createNormalUser(UserBasicDTO user) throws RokaMokaContentDuplicated;
}
