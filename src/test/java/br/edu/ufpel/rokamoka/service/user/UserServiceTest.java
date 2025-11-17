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
import br.edu.ufpel.rokamoka.repository.RoleRepository;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.MockUserSession;
import br.edu.ufpel.rokamoka.service.device.DeviceService;
import br.edu.ufpel.rokamoka.service.mokadex.MokadexService;
import org.apache.commons.lang3.StringUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link UserService} class, which is responsible for handling user-related API operations.
 *
 * @author MauricioMucci
 * @see UserService
 * @see UserRepository
 * @see RoleRepository
 * @see AuthenticationService
 * @see MokadexService
 * @see DeviceService
 * @see PasswordEncoder
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest implements MockUserSession, MockRepository<User> {

    @InjectMocks private UserService userService;

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;

    @Mock private AuthenticationService authenticationService;
    @Mock private MokadexService mokadexService;
    @Mock private DeviceService deviceService;

    @Mock private PasswordEncoder passwordEncoder;

    //region createNormalUser
    @Test
    void createNormalUser_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByEmail() {
        // Arrange
        UserBasicDTO userDTO = Instancio.create(UserBasicDTO.class);

        when(this.userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class, () -> this.userService.createNormalUser(userDTO));

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).existsByEmail(anyString());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService);
    }

    @Test
    void createNormalUser_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByName() {
        // Arrange
        UserBasicDTO userDTO = Instancio.create(UserBasicDTO.class);

        when(this.userRepository.existsByNome(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class, () -> this.userService.createNormalUser(userDTO));

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).existsByNome(anyString());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService);
    }

    static Stream<Arguments> provideCreateNormalUserInput() {
        UserBasicDTO withDeviceId = Instancio.create(UserBasicDTO.class);
        UserBasicDTO ignoreDeviceId = Instancio.of(UserBasicDTO.class).ignore(field(UserBasicDTO::deviceId)).create();
        Role userRole = Instancio.of(Role.class).set(field(Role::getName), RoleEnum.USER).create();
        return Stream.of(Arguments.of(withDeviceId, userRole), Arguments.of(ignoreDeviceId, userRole),
                Arguments.of(withDeviceId, null), Arguments.of(ignoreDeviceId, null));
    }

    @ParameterizedTest
    @MethodSource("provideCreateNormalUserInput")
    void createNormalUser_shouldSaveNewUser_whenInputIsValid(UserBasicDTO userDTO, Role userRole)
    throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.passwordEncoder.encode(userDTO.password())).thenReturn("encodedPassword");
        when(this.roleRepository.findByName(RoleEnum.USER)).thenReturn(userRole);

        when(this.userRepository.save(any(User.class))).thenAnswer(inv -> this.mockRepositorySave(inv.getArgument(0)));
        doAnswer(inv -> null).when(this.mokadexService).getOrCreateMokadexByUser(any(User.class));

        if (StringUtils.isNotBlank(userDTO.deviceId())) {
            when(this.deviceService.save(anyString(), any(User.class))).thenReturn(mock(Device.class));
        }

        when(this.authenticationService.basicAuthenticationAndGenerateJWT(anyString(), anyString())).thenReturn("jwt");

        // Act
        UserAuthDTO actualOutput = this.userService.createNormalUser(userDTO);

        // Assert
        assertNotNull(actualOutput);
        assertEquals("jwt", actualOutput.jwt());

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).save(any(User.class));
        verify(this.mokadexService).getOrCreateMokadexByUser(any(User.class));

        if (StringUtils.isNotBlank(userDTO.deviceId())) {
            verify(this.deviceService).save(anyString(), any(User.class));
        } else {
            verifyNoInteractions(this.deviceService);
        }

        verify(this.authenticationService).basicAuthenticationAndGenerateJWT(anyString(), anyString());
    }
    //endregion

    //region createAnonymousUser
    @Test
    void createAnonymousUser_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByName() {
        // Arrange
        UserAnonymousRequestDTO userDTO = Instancio.create(UserAnonymousRequestDTO.class);

        when(this.userRepository.existsByNome(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class, () -> this.userService.createAnonymousUser(userDTO));

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).existsByNome(anyString());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService);
    }

    static Stream<Arguments> provideAnonymousUserInput() {
        UserAnonymousRequestDTO withDeviceId = Instancio.create(UserAnonymousRequestDTO.class);
        UserAnonymousRequestDTO ignoreDeviceId = Instancio.of(UserAnonymousRequestDTO.class)
                .ignore(field(UserAnonymousRequestDTO::deviceId))
                .create();
        Role userRole = Instancio.of(Role.class).set(field(Role::getName), RoleEnum.USER).create();
        return Stream.of(Arguments.of(withDeviceId, userRole), Arguments.of(ignoreDeviceId, userRole),
                Arguments.of(withDeviceId, null), Arguments.of(ignoreDeviceId, null));
    }

    @ParameterizedTest
    @MethodSource("provideAnonymousUserInput")
    void createAnonymousUser_shouldSaveNewAnonymousUser_whenInputIsValid(UserAnonymousRequestDTO userDTO, Role userRole)
    throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(this.roleRepository.findByName(RoleEnum.USER)).thenReturn(userRole);

        when(this.userRepository.save(any(User.class))).thenAnswer(inv -> this.mockRepositorySave(inv.getArgument(0)));
        doAnswer(inv -> null).when(this.mokadexService).getOrCreateMokadexByUser(any(User.class));

        if (StringUtils.isNotBlank(userDTO.deviceId())) {
            when(this.deviceService.save(anyString(), any(User.class))).thenReturn(mock(Device.class));
        }

        when(this.authenticationService.basicAuthenticationAndGenerateJWT(anyString(), anyString())).thenReturn("jwt");

        // Act
        UserAnonymousResponseDTO actualOutput = this.userService.createAnonymousUser(userDTO);

        // Assert
        assertNotNull(actualOutput);
        assertEquals("jwt", actualOutput.jwt());

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).save(any(User.class));
        verify(this.mokadexService).getOrCreateMokadexByUser(any(User.class));

        if (StringUtils.isNotBlank(userDTO.deviceId())) {
            verify(this.deviceService).save(anyString(), any(User.class));
        } else {
            verifyNoInteractions(this.deviceService);
        }

        verify(this.authenticationService).basicAuthenticationAndGenerateJWT(anyString(), anyString());
    }
    //endregion

    //region resetUserPassword
    @Test
    void resetUserPassword_shouldThrowRokaMokaContentNotFoundException_whenUserDoesNotExistByNameAndEmail() {
        // Arrange
        UserResetPasswordDTO userDTO = Instancio.create(UserResetPasswordDTO.class);

        when(this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())).thenReturn(false);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.userService.resetUserPassword(userDTO));

        verify(this.userRepository).existsByNomeAndEmail(userDTO.name(), userDTO.email());
        verifyNoMoreInteractions(this.userRepository);
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService, this.roleRepository,
                this.passwordEncoder);
    }

    @Test
    void resetUserPassword_shouldThrowRokaMokaContentNotFoundException_whenUserIsNotFoundByName() {
        // Arrange
        UserResetPasswordDTO userDTO = Instancio.create(UserResetPasswordDTO.class);

        when(this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())).thenReturn(true);
        when(this.userRepository.findByNome(userDTO.name())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.userService.resetUserPassword(userDTO));

        verify(this.userRepository).existsByNomeAndEmail(userDTO.name(), userDTO.email());
        verify(this.userRepository).findByNome(userDTO.name());
        verifyNoMoreInteractions(this.userRepository);
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService, this.roleRepository,
                this.passwordEncoder);
    }

    @Test
    void resetUserPassword_shouldThrowRokaMokaForbiddenException_whenOldPasswordDoesNotMatch() {
        // Arrange
        UserResetPasswordDTO userDTO = Instancio.create(UserResetPasswordDTO.class);
        User foundByName = Instancio.create(User.class);

        when(this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())).thenReturn(true);
        when(this.userRepository.findByNome(userDTO.name())).thenReturn(Optional.of(foundByName));
        when(this.passwordEncoder.matches(userDTO.oldPassword(), foundByName.getSenha())).thenReturn(false);

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class, () -> this.userService.resetUserPassword(userDTO));

        verify(this.userRepository).existsByNomeAndEmail(userDTO.name(), userDTO.email());
        verify(this.userRepository).findByNome(userDTO.name());
        verify(this.passwordEncoder).matches(userDTO.oldPassword(), foundByName.getSenha());
        verifyNoMoreInteractions(this.userRepository);
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService, this.roleRepository);
    }

    @Test
    void resetUserPassword_shouldThrowRokaMokaContentNotFoundException_whenLoggedInUserIsNotFound() {
        // Arrange
        UserResetPasswordDTO userDTO = Instancio.create(UserResetPasswordDTO.class);
        User foundByName = Instancio.create(User.class);
        ServiceContext mockContext = this.mockServiceContext();

        when(this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())).thenReturn(true);
        when(this.userRepository.findByNome(userDTO.name())).thenReturn(Optional.of(foundByName));
        when(this.passwordEncoder.matches(userDTO.oldPassword(), foundByName.getSenha())).thenReturn(true);
        when(this.userRepository.findByNome(LOGGED_USER_NAME)).thenReturn(Optional.empty());

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentNotFoundException.class, () -> this.userService.resetUserPassword(userDTO));
        }

        verify(this.userRepository).existsByNomeAndEmail(userDTO.name(), userDTO.email());
        verify(this.userRepository, times(2)).findByNome(anyString());
        verify(this.passwordEncoder).matches(userDTO.oldPassword(), foundByName.getSenha());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService, this.roleRepository);
    }

    @Test
    void resetUserPassword_shouldThrowRokaMokaForbiddenException_whenLoggedInUserDiffersFromInput() {
        // Arrange
        UserResetPasswordDTO userDTO = Instancio.create(UserResetPasswordDTO.class);
        User foundByName = Instancio.create(User.class);
        User loggedIn = Instancio.create(User.class);
        ServiceContext mockContext = this.mockServiceContext();

        when(this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())).thenReturn(true);
        when(this.userRepository.findByNome(userDTO.name())).thenReturn(Optional.of(foundByName));
        when(this.passwordEncoder.matches(userDTO.oldPassword(), foundByName.getSenha())).thenReturn(true);
        when(this.userRepository.findByNome(LOGGED_USER_NAME)).thenReturn(Optional.of(loggedIn));

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaForbiddenException.class, () -> this.userService.resetUserPassword(userDTO));
        }

        verify(this.userRepository).existsByNomeAndEmail(userDTO.name(), userDTO.email());
        verify(this.userRepository, times(2)).findByNome(anyString());
        verify(this.passwordEncoder).matches(userDTO.oldPassword(), foundByName.getSenha());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService, this.roleRepository);
    }

    @Test
    void resetUserPassword_shouldResetUserPassword_whenInputIsValid()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        UserResetPasswordDTO userDTO = Instancio.create(UserResetPasswordDTO.class);
        User loggedIn = Instancio.create(User.class);
        ServiceContext mockContext = this.mockServiceContext();

        when(this.userRepository.existsByNomeAndEmail(userDTO.name(), userDTO.email())).thenReturn(true);
        when(this.userRepository.findByNome(userDTO.name())).thenReturn(Optional.of(loggedIn));
        when(this.passwordEncoder.matches(userDTO.oldPassword(), loggedIn.getSenha())).thenReturn(true);

        when(this.userRepository.findByNome(LOGGED_USER_NAME)).thenReturn(Optional.of(loggedIn));
        when(this.passwordEncoder.encode(userDTO.newPassword())).thenReturn("encodedPassword");

        // Act
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            this.userService.resetUserPassword(userDTO);
        }

        // Assert
        verify(this.userRepository).existsByNomeAndEmail(userDTO.name(), userDTO.email());
        verify(this.userRepository, times(2)).findByNome(anyString());
        verify(this.passwordEncoder).matches(anyString(), anyString());
        verify(this.userRepository).save(loggedIn);
        verify(this.passwordEncoder).encode(userDTO.newPassword());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService, this.roleRepository);
    }
    //endregion

    //region getLoggedUserInformation
    @Test
    void getLoggedUserInformation_shouldThrowRokaMokaContentNotFoundException_whenLoggedInUserIsNotFound() {
        // Arrange
        ServiceContext mockContext = this.mockServiceContext();

        when(this.userRepository.findByNome(LOGGED_USER_NAME)).thenReturn(Optional.empty());

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            assertThrows(RokaMokaContentNotFoundException.class, () -> this.userService.getLoggedUserInformation());
        }

        verify(this.userRepository).findByNome(LOGGED_USER_NAME);
        verifyNoMoreInteractions(this.userRepository);
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService, this.roleRepository);
    }

    @Test
    void getLoggedUserInformation_shouldReturnUserInformation_whenLoggedInUserIsFound()
    throws RokaMokaContentNotFoundException {
        // Arrange
        User loggedIn = Instancio.create(User.class);
        ServiceContext mockContext = this.mockServiceContext();

        Mokadex mokadex = Instancio.create(Mokadex.class);
        MokadexOutputDTO expectedOutput = Instancio.create(MokadexOutputDTO.class);

        when(this.mokadexService.getOrCreateMokadexByUser(loggedIn)).thenReturn(mokadex);
        when(this.mokadexService.getMokadexOutputDTOByMokadex(mokadex)).thenReturn(expectedOutput);
        when(this.userRepository.findByNome(LOGGED_USER_NAME)).thenReturn(Optional.of(loggedIn));

        // Act & Assert
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            UserOutputDTO actualUserOutput = this.userService.getLoggedUserInformation();
            assertNotNull(actualUserOutput);
            assertEquals(loggedIn.getNome(), actualUserOutput.name());
            assertEquals(loggedIn.getEmail(), actualUserOutput.email());
            assertEquals(loggedIn.getRole().getName().getDescription(), actualUserOutput.role());

            MokadexOutputDTO actualMokadex = actualUserOutput.mokaDex();
            assertNotNull(actualMokadex);
            assertEquals(expectedOutput, actualMokadex);
        }

        verify(this.userRepository).findByNome(LOGGED_USER_NAME);
        verify(this.mokadexService).getOrCreateMokadexByUser(loggedIn);
        verify(this.mokadexService).getMokadexOutputDTOByMokadex(mokadex);
    }
    //endregion

    //region getByNome
    @Test
    void getByNome_shouldReturnUser_whenUserExistsByName() throws RokaMokaContentNotFoundException {
        // Arrange
        User user = mock(User.class);

        when(this.userRepository.findByNome(anyString())).thenReturn(Optional.of(user));

        // Act
        User actual = this.userService.getByNome("");

        // Assert
        assertNotNull(actual);
        assertEquals(user, actual);

        verify(this.userRepository).findByNome(anyString());
    }

    @Test
    void getByNome_shouldThrowRokaMokaContentNotFoundException_whenUserDoesNotExistByName() {
        // Arrange
        when(this.userRepository.findByNome(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.userService.getByNome(anyString()));

        verify(this.userRepository).findByNome(anyString());
    }
    //endregion

    //region createResearcher
    @Test
    void createResearcher_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByEmail() {
        // Arrange
        UserBasicDTO userDTO = Instancio.create(UserBasicDTO.class);

        when(this.userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class, () -> this.userService.createResearcher(userDTO));

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).existsByEmail(anyString());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService);
    }

    @Test
    void createResearcher_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByName() {
        // Arrange
        UserBasicDTO userDTO = Instancio.create(UserBasicDTO.class);

        when(this.userRepository.existsByNome(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class, () -> this.userService.createResearcher(userDTO));

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).existsByNome(anyString());
        verifyNoInteractions(this.mokadexService, this.deviceService, this.authenticationService);
    }

    static Stream<Arguments> provideCreateResearcherUserInput() {
        UserBasicDTO withDeviceId = Instancio.create(UserBasicDTO.class);
        UserBasicDTO ignoreDeviceId = Instancio.of(UserBasicDTO.class).ignore(field(UserBasicDTO::deviceId)).create();
        Role researcherRole = Instancio.of(Role.class).set(field(Role::getName), RoleEnum.RESEARCHER).create();
        return Stream.of(Arguments.of(withDeviceId, researcherRole), Arguments.of(ignoreDeviceId, researcherRole),
                Arguments.of(withDeviceId, null), Arguments.of(ignoreDeviceId, null));
    }

    @ParameterizedTest
    @MethodSource("provideCreateResearcherUserInput")
    void createResearcher_shouldSaveNewUser_whenInputIsValid(UserBasicDTO userDTO, Role researcherRole)
    throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.passwordEncoder.encode(userDTO.password())).thenReturn("encodedPassword");
        when(this.roleRepository.findByName(RoleEnum.RESEARCHER)).thenReturn(researcherRole);

        when(this.userRepository.save(any(User.class))).thenAnswer(inv -> this.mockRepositorySave(inv.getArgument(0)));
        doAnswer(inv -> null).when(this.mokadexService).getOrCreateMokadexByUser(any(User.class));

        if (StringUtils.isNotBlank(userDTO.deviceId())) {
            when(this.deviceService.save(anyString(), any(User.class))).thenReturn(mock(Device.class));
        }

        when(this.authenticationService.basicAuthenticationAndGenerateJWT(anyString(), anyString())).thenReturn("jwt");

        // Act
        UserAuthDTO actualOutput = this.userService.createResearcher(userDTO);

        // Assert
        assertNotNull(actualOutput);
        assertEquals("jwt", actualOutput.jwt());

        verify(this.passwordEncoder).encode(anyString());
        verify(this.roleRepository).findByName(any(RoleEnum.class));
        verify(this.userRepository).save(any(User.class));
        verify(this.mokadexService).getOrCreateMokadexByUser(any(User.class));

        if (StringUtils.isNotBlank(userDTO.deviceId())) {
            verify(this.deviceService).save(anyString(), any(User.class));
        } else {
            verifyNoInteractions(this.deviceService);
        }

        verify(this.authenticationService).basicAuthenticationAndGenerateJWT(anyString(), anyString());
    }
    //endregion

    //region updateRole
    @ParameterizedTest
    @EnumSource(RoleEnum.class)
    void updateRole_shouldUpdateUserRole_whenCalled(RoleEnum roleEnum) {
        // Arrange
        User user = new User();
        User spyUser = spy(user);

        Role role = Instancio.of(Role.class).set(field(Role::getName), roleEnum).create();

        // Act
        this.userService.updateRole(spyUser, role);

        // Assert
        assertEquals(role, spyUser.getRole());

        verify(this.userRepository).save(spyUser);
    }
    //endregion
}
