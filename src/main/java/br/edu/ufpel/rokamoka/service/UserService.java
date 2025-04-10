package br.edu.ufpel.rokamoka.service;


import br.edu.ufpel.rokamoka.dto.input.NewUserDTO;
import br.edu.ufpel.rokamoka.dto.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicated;


public interface UserService {

    UserResponseDTO createNormalUser(NewUserDTO user) throws RokaMokaContentDuplicated;
}
