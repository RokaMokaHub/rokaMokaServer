package br.edu.ufpel.rokamoka.service.user;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.Role;
import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAuthDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.RoleRepository;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.mokadex.MokadexService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Implementation of the {@link IUserService} interface.
 *
 * <p>This service provides operations related to user management, including
 * creating users and ensuring their uniqueness in the system. It also handles authentication processes by collaborating
 * with {@link AuthenticationService}.
 *
 * <p>It uses a {@link UserRepository} to interact with the database for user-related
 * operations. The passwords are securely encoded using a {@link PasswordEncoder} before storing them in the database.
 *
 * <p>To maintain data integrity, this service validates user information before
 * creating a user, and throws a {@link RokaMokaContentDuplicatedException} if the provided email or username is already
 * in use.
 *
 * <p>This class is marked as a Spring {@link Service} and uses constructor injection
 * to receive its dependencies.
 *
 * @author iyisakuma
 * @see IUserService
 * @see UserRepository
 * @see AuthenticationService
 * @see PasswordEncoder
 * @see RokaMokaContentDuplicatedException
 */
@Slf4j
@Service
@Validated
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final MokadexService mokadexService;

    /**
     * Creates a normal user with the provided user information and generates a JWT.
     *
     * <p>This method takes a {@link UserBasicDTO} object containing the user's name, email,
     * and password, and creates a new user in the system. The password is encoded before storing the user. If the email
     * or name is already in use, a {@link RokaMokaContentDuplicatedException} is thrown. Upon successful creation, the
     * user is authenticated, and a JWT is generated and returned.
     *
     * @param userDTO A {@link UserBasicDTO} containing the user's name, email, and password.
     *
     * @return A {@link UserAuthDTO} containing the generated JWT.
     * @throws RokaMokaContentDuplicatedException if the user's email or name already exists.
     */
    @Override
    public UserAuthDTO createNormalUser(@Valid UserBasicDTO userDTO) throws RokaMokaContentDuplicatedException {
        var undecodedPasswd = userDTO.password();
        var user = User.builder()
                .nome(userDTO.name())
                .email(userDTO.email())
                .senha(this.passwordEncoder.encode(undecodedPasswd))
                .role(this.roleRepository.findByName(RoleEnum.USER))
                .build();

        this.validateOrThrowException(user);
        User newUser = this.userRepository.save(user);

        String userJWT =
                this.authenticationService.basicAuthenticationAndGenerateJWT(newUser.getNome(), undecodedPasswd);
        return new UserAuthDTO(userJWT);
    }

    /**
     * Creates an anonymous user with the provided username and generates a JWT.
     * <p>This method takes a {@link UserAnonymousRequestDTO} object containing the user's name,
     * generates a random password, and creates a new user in the system. The user is then authenticated, and a JWT is
     * generated and returned along with the user's name.
     *
     * @param userDTO A {@link UserAnonymousRequestDTO} containing the user's name.
     *
     * @return A {@link UserAnonymousResponseDTO} containing the generated JWT and the user's name.
     * @throws RokaMokaContentDuplicatedException if the user's name already exists.
     */
    @Override
    public UserAnonymousResponseDTO createAnonymousUser(@Valid UserAnonymousRequestDTO userDTO)
            throws RokaMokaContentDuplicatedException {
        var undecodedPasswd = this.generateRandomPassword();
        var user = User.builder()
                .nome(userDTO.userName())
                .senha(this.passwordEncoder.encode(undecodedPasswd))
                .role(this.roleRepository.findByName(RoleEnum.USER))
                .build();

        this.validateOrThrowException(user);

        User newUser = this.userRepository.save(user);
        String userJWT =
                this.authenticationService.basicAuthenticationAndGenerateJWT(newUser.getNome(), undecodedPasswd);
        return new UserAnonymousResponseDTO(userJWT, undecodedPasswd);
    }

    /**
     * Resets the password for a specific user.
     *
     * <p>This method validates that the user identified by the provided {@link UserBasicDTO} exists in the system. It
     * ensures that the currently authenticated user matches the user whose password is being reset, enforcing a
     * self-service password reset policy.
     *
     * @param userDTO A {@link UserBasicDTO} containing the user's credentials.
     *
     * @throws RokaMokaContentNotFoundException if the user specified in the request is not found.
     * @throws RokaMokaForbiddenException if the user does not have permission to perform this action.
     */
    @Override
    public void resetUserPassword(@Valid UserResetPasswordDTO userDTO)
            throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        User user = getResetPasswordUser(userDTO);
        user.setSenha(this.passwordEncoder.encode(userDTO.newPassword()));
        this.userRepository.save(user);
    }

    private User getResetPasswordUser(UserResetPasswordDTO userDTO)
            throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        if (!this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())) {
            throw new RokaMokaContentNotFoundException("Nome de usuário ou email inválido");
        }
        User user = this.userRepository
                .findByNome(userDTO.name())
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Usuário não encontrado"));
        if (!this.passwordEncoder.matches(userDTO.oldPassword(), user.getSenha())) {
            throw new RokaMokaForbiddenException("A senha informada é inválida");
        }
        User loggedUser = this.userRepository
                .findByNome(ServiceContext.getContext().getUser().getUsername())
                .orElseThrow(() -> new RokaMokaForbiddenException("Usuário logado não encontrado"));
        if (!loggedUser.equals(user)) {
            throw new RokaMokaForbiddenException("Apenas o próprio usuário pode alterar a sua senha");
        }
        return user;
    }

    @Override
    public UserOutputDTO getLoggedUserInformation() throws RokaMokaForbiddenException {
        log.info("Buscando as informações do usuário logado");

        User loggedUser = this.userRepository
                .findByNome(ServiceContext.getContext().getUser().getUsername())
                .orElseThrow(() -> new RokaMokaForbiddenException("Usuário logado não encontrado"));
        Optional<Mokadex> maybeMokadex = mokadexService.getMokadexByUsuario(loggedUser);

        if (maybeMokadex.isPresent()) {

            log.info("Retornando as informações com {}", Mokadex.class.getSimpleName());
            Mokadex mokadex = maybeMokadex.get();
            MokadexOutputDTO mokadexOutputDTO = mokadexService.buildMokadexOutputDTOByMokadex(mokadex);
            return new UserOutputDTO(loggedUser, mokadexOutputDTO);

        }

        log.info("Retornando as informações sem {}", Mokadex.class.getSimpleName());
        return new UserOutputDTO(loggedUser);
    }

    @Override
    public UserAuthDTO createReseacher(UserBasicDTO userDTO) throws RokaMokaContentDuplicatedException {
        var undecodedPasswd = userDTO.password();
        var user = User.builder()
                .nome(userDTO.name())
                .email(userDTO.email())
                .senha(this.passwordEncoder.encode(undecodedPasswd))
                .role(this.roleRepository.findByName(RoleEnum.RESEARCHER))
                .build();

        this.validateOrThrowException(user);
        User newUser = this.userRepository.save(user);

        String userJWT =
                this.authenticationService.basicAuthenticationAndGenerateJWT(newUser.getNome(), undecodedPasswd);
        return new UserAuthDTO(userJWT);
    }

    @Override
    public void updateRole(User requester, Role role) {
        requester.setRole(role);
        this.userRepository.save(requester);
    }

    /**
     * Validates that the user's email and name are not already in use.
     *
     * <p>This method checks the repository to ensure that the email and name
     * of the given user do not already exist in the database. If either the email or the name is found to be in use, a
     * {@link RokaMokaContentDuplicatedException} is thrown to indicate the duplication.
     *
     * @param user The {@link User} object containing the email and name to validate.
     *
     * @throws RokaMokaContentDuplicatedException if the user's email or name already exists.
     */
    private void validateOrThrowException(User user) throws RokaMokaContentDuplicatedException {
        if (user.getEmail() != null && this.userRepository.existsByEmail(user.getEmail())) {
            throw new RokaMokaContentDuplicatedException("O email já está sendo utilizado,");
        }
        if (user.getNome() != null && this.userRepository.existsByNome(user.getNome())) {
            throw new RokaMokaContentDuplicatedException("O nome do usuário já está sendo utilizado");
        }
    }

    /**
     * Generates a random password consisting of 10 lowercase letters.
     *
     * @return A random password consisting of 10 lowercase letters.
     */
    private String generateRandomPassword() {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        return pwdGenerator.generate(10);
    }
}
