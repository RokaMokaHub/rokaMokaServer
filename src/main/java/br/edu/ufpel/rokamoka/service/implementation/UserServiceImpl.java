package br.edu.ufpel.rokamoka.service.implementation;


import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

/**
 * Implementation of the {@link UserService} interface.
 *
 * <p>This service provides operations related to user management, including
 * creating users and ensuring their uniqueness in the system. It also handles
 * authentication processes by collaborating with {@link AuthenticationService}.
 *
 * <p>It uses a {@link UserRepository} to interact with the database for user-related
 * operations. The passwords are securely encoded using a {@link PasswordEncoder}
 * before storing them in the database.
 *
 * <p>To maintain data integrity, this service validates user information before
 * creating a user, and throws a {@link RokaMokaContentDuplicatedException} if the
 * provided email or username is already in use.
 *
 * <p>This class is marked as a Spring {@link Service} and uses constructor injection
 * to receive its dependencies.
 *
 * @author iyisakuma
 * @see UserService
 * @see UserRepository
 * @see AuthenticationService
 * @see PasswordEncoder
 * @see RokaMokaContentDuplicatedException
 */
@Service
@Validated
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a normal user with the provided user information and generates a JWT.
     *
     * <p>This method takes a {@link UserBasicDTO} object containing the user's name, email,
     * and password, and creates a new user in the system. The password is encoded before
     * storing the user. If the email or name is already in use, a
     * {@link RokaMokaContentDuplicatedException} is thrown. Upon successful creation,
     * the user is authenticated, and a JWT is generated and returned.
     *
     * @param userDTO A {@link UserBasicDTO} containing the user's name, email, and password.
     * @return A {@link UserResponseDTO} containing the generated JWT.
     * @throws RokaMokaContentDuplicatedException if the user's email or name already exists.
     */
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

    /**
     * Validates that the user's email and name are not already in use.
     *
     * <p>This method checks the repository to ensure that the email and name
     * of the given user do not already exist in the database. If either the
     * email or the name is found to be in use, a {@link RokaMokaContentDuplicatedException}
     * is thrown to indicate the duplication.
     *
     * @param user The {@link User} object containing the email and name to validate.
     * @throws RokaMokaContentDuplicatedException if the user's email or name already exists.
     */
    private void validateOrThrowExecption(User user) throws RokaMokaContentDuplicatedException {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new RokaMokaContentDuplicatedException("O email já está sendo utilizado,");
        }
        if (this.userRepository.existsByName(user.getNome())) {
            throw new RokaMokaContentDuplicatedException("O nome do usuário já está sendo utilizado");
        }
    }
}
