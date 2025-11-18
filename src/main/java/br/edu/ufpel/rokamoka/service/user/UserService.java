package br.edu.ufpel.rokamoka.service.user;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.Device;
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
import br.edu.ufpel.rokamoka.exceptions.RokaMokaNoUserInContextException;
import br.edu.ufpel.rokamoka.repository.RoleRepository;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.device.IDeviceService;
import br.edu.ufpel.rokamoka.service.mokadex.MokadexService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.RandomStringGenerator.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

/**
 * Service implementation of the {@link IUserService} interface for managing operations on the {@link User} resource.
 *
 * @author iyisakuma
 * @see IUserService
 * @see UserRepository
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final MokadexService mokadexService;
    private final IDeviceService deviceService;

    /**
     * Creates a normal user with the provided user information and generates a JWT.
     *
     * @param userDTO A {@code userDTO} containing the user's name, email, and password.
     *
     * @return A {@link UserAuthDTO} containing the generated JWT.
     * @throws RokaMokaContentDuplicatedException if the user's email or name already exists.
     */
    @Override
    public UserAuthDTO createNormalUser(@Valid UserBasicDTO userDTO) {
        var undecodedPasswd = userDTO.password();
        var user = User.builder()
                .nome(userDTO.name())
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .senha(this.passwordEncoder.encode(undecodedPasswd))
                .role(this.roleRepository.findByName(RoleEnum.USER))
                .build();

        this.validateOrThrowException(user);
        User newUser = this.save(user);
        this.saveUserDevice(userDTO.deviceId(), newUser);

        String userJWT = this.authenticationService.basicAuthenticationAndGenerateJWT(newUser.getNome(),
                undecodedPasswd);
        return new UserAuthDTO(userJWT);
    }

    private User save(User user) {
        User newUser = this.userRepository.save(user);
        this.mokadexService.getOrCreateMokadexByUser(user);
        return newUser;
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
    public UserAnonymousResponseDTO createAnonymousUser(@Valid UserAnonymousRequestDTO userDTO) {
        var undecodedPasswd = generateRandomPassword();
        var user = User.builder()
                .nome(userDTO.userName())
                .senha(this.passwordEncoder.encode(undecodedPasswd))
                .role(this.roleRepository.findByName(RoleEnum.USER))
                .build();

        this.validateOrThrowException(user);
        User newUser = this.save(user);
        this.saveUserDevice(userDTO.deviceId(), newUser);

        String userJWT = this.authenticationService.basicAuthenticationAndGenerateJWT(newUser.getNome(),
                undecodedPasswd);
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
    public void resetUserPassword(@Valid UserResetPasswordDTO userDTO) {
        User user = this.getResetPasswordUser(userDTO);
        user.setSenha(this.passwordEncoder.encode(userDTO.newPassword()));
        this.userRepository.save(user);
    }

    private User getResetPasswordUser(UserResetPasswordDTO userDTO) {
        if (!this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())) {
            throw new RokaMokaContentNotFoundException("Nome de usuário ou email inválido");
        }
        User user = this.userRepository.findByNome(userDTO.name())
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Usuário não encontrado"));
        if (!this.passwordEncoder.matches(userDTO.oldPassword(), user.getSenha())) {
            throw new RokaMokaForbiddenException("A senha informada é inválida");
        }
        User loggedUser = this.findLoggedUser();
        if (!loggedUser.equals(user)) {
            throw new RokaMokaForbiddenException("Apenas o próprio usuário pode alterar a sua senha");
        }
        return user;
    }

    /**
     * Retrieves the information of the currently logged-in user.
     *
     * @return A {@code UserOutputDTO} containing the information of the logged-in user. If a Mokadex is associated with
     * the user, its details will also be included; otherwise, only basic user details are provided.
     * @throws RokaMokaContentNotFoundException If the logged user cannot be found in the repository.
     */
    @Override
    public UserOutputDTO getLoggedUserInformation() {
        log.info("Buscando as informações do usuário logado");

        User loggedUser = this.findLoggedUser();
        Mokadex mokadex = this.mokadexService.getOrCreateMokadexByUser(loggedUser);
        MokadexOutputDTO mokadexOutputDTO = this.mokadexService.getMokadexOutputDTOByMokadex(mokadex);

        log.info("Informações do usuário logado retornadas com sucesso");
        return new UserOutputDTO(loggedUser, mokadexOutputDTO);
    }

    @Override
    public User getByNome(String nome) {
        return this.userRepository.findByNome(nome)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Usuário não encontrado"));
    }

    @Override
    public User getLoggedUserOrElseThrow() {
        try {
            return this.userRepository.findByNome(ServiceContext.getContext().getUsernameOrThrow())
                    .orElseThrow(() -> new RokaMokaContentNotFoundException("Usuário logado não encontrado"));
        } catch (RokaMokaNoUserInContextException e) {
            throw new RokaMokaForbiddenException("Usuário deve existir no contexto");
        }
    }

    private User findLoggedUser() {
        return this.userRepository.findByNome(ServiceContext.getContext().getUser().getUsername())
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Usuário logado não encontrado"));
    }

    @Override
    public UserAuthDTO createResearcher(@Valid UserBasicDTO userDTO) {
        var undecodedPasswd = userDTO.password();
        var user = User.builder()
                .nome(userDTO.name())
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .senha(this.passwordEncoder.encode(undecodedPasswd))
                .role(this.roleRepository.findByName(RoleEnum.RESEARCHER))
                .build();

        this.validateOrThrowException(user);
        User newUser = this.save(user);
        this.saveUserDevice(userDTO.deviceId(), newUser);

        String userJWT = this.authenticationService.basicAuthenticationAndGenerateJWT(newUser.getNome(),
                undecodedPasswd);
        return new UserAuthDTO(userJWT);
    }

    private Optional<Device> saveUserDevice(String deviceId, User newUser) {
        return StringUtils.isNotBlank(deviceId)
               ? Optional.ofNullable(this.deviceService.save(deviceId, newUser))
               : Optional.empty();
    }

    @Override
    public void updateRole(@NotNull User requester, Role role) {
        requester.setRole(role);
        this.userRepository.save(requester);
    }

    /**
     * Validates that the user's email and name are not already in use.
     *
     * @param user The {@code user} entity who contains the email and name to validate.
     *
     * @throws RokaMokaContentDuplicatedException if the user's email or name already exists.
     */
    private void validateOrThrowException(User user) {
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
    private static String generateRandomPassword() {
        RandomStringGenerator pwdGenerator = new Builder().withinRange('a', 'z').build();
        return pwdGenerator.generate(10);
    }
}
