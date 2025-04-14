package br.edu.ufpel.rokamoka.service.implementation;


import java.util.ArrayList;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@Service
@Validated
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createNormalUser(@Valid UserBasicDTO userDTO) throws RokaMokaContentDuplicatedException {
        var undecodedPasswd = userDTO.password();
        var user = User.builder().nome(userDTO.name()).email(userDTO.email())
                .senha(this.passwordEncoder.encode(undecodedPasswd))
                .roles(new ArrayList<>()).build();

        this.validateOrThrowExecption(user);
        User newUser = this.userRepository.save(user);

        String userJWT =
                this.authenticationService.basicAuthenticationAndGenerateJWT(newUser.getNome(), undecodedPasswd);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(newUser.getNome(), userDTO.password());
        return new UserResponseDTO(userJWT);
    }

    private void validateOrThrowExecption(User user) throws RokaMokaContentDuplicatedException {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new RokaMokaContentDuplicatedException("O email já está sendo utilizado,");
        }
        if (this.userRepository.existsByName(user.getNome())) {
            throw new RokaMokaContentDuplicatedException("O nome do usuário já está sendo utilizado");
        }
    }
}
