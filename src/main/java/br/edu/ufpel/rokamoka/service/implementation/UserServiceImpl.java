package br.edu.ufpel.rokamoka.service.implementation;


import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.input.NewUserDTO;
import br.edu.ufpel.rokamoka.dto.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicated;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.UserService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createNormalUser(NewUserDTO userDTO) throws RokaMokaContentDuplicated {
        var user = User.builder().nome(userDTO.name()).email(userDTO.email())
                .senha(passwordEncoder.encode(userDTO.password())).roles(new ArrayList<>()).build();
        isValid(user);
        User newUser = this.userRepository.save(user);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(newUser.getNome(), userDTO.password());

        Authentication authentication = this.authenticationManager.authenticate(authToken);

        var jwtToken = this.authenticationService.authenticate(authentication);
        return new UserResponseDTO(jwtToken);
    }

    private void isValid(User user) throws RokaMokaContentDuplicated {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new RokaMokaContentDuplicated("O email já existe no banco de dados");
        }
    }
}
