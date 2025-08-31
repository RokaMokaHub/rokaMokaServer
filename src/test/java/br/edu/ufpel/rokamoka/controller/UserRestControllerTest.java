package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAuthDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.user.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link UserRestController} class, which is responsible for handling user-related API operations.
 *
 * @author MauricioMucci
 * @see UserRestController
 * @see UserService
 * @see AuthenticationService
 */
@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @InjectMocks private UserRestController userRestController;

    @Mock private UserService userService;

    @Mock private AuthenticationService authenticationService;

    //region register
    @Test
    void register_shouldReturnTokenGeneratedForNewUser_whenSuccessful() throws RokaMokaContentDuplicatedException {
        // Arrange
        UserAuthDTO expectedOutput = Instancio.create(UserAuthDTO.class);

        when(this.userService.createNormalUser(any(UserBasicDTO.class)))
                .thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<UserAuthDTO>> response =
                this.userRestController.register(mock(UserBasicDTO.class));

        // Assert
        this.assertSuccessfulResponse(response, expectedOutput);

        verify(this.userService, times(1)).createNormalUser(any(UserBasicDTO.class));
        verifyNoInteractions(this.authenticationService);
    }

    private <T> void assertSuccessfulResponse(ResponseEntity<ApiResponseWrapper<T>> response, T expected) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<T> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        T actual = body.getBody();
        if (expected != null) {
            assertNotNull(actual, "Output atual não deve ser null");
            assertEquals(expected, actual, "Output atual deve conter atributos esperados");
        } else {
            assertNull(actual, "Output atual deve ser null");
        }
    }

    @Test
    void register_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByEmailOrName()
    throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.userService.createNormalUser(any(UserBasicDTO.class)))
                .thenThrow(RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.userRestController.register(mock(UserBasicDTO.class)));

        verify(this.userService, times(1))
                .createNormalUser(any(UserBasicDTO.class));
        verifyNoInteractions(this.authenticationService);
    }
    //endregion

    //region anonymousRegister
    @Test
    void anonymousRegister_shouldReturnAnonymousToken_whenSuccessful() throws RokaMokaContentDuplicatedException {
        // Arrange
        UserAnonymousResponseDTO expectedOutput = Instancio.create(UserAnonymousResponseDTO.class);

        when(this.userService.createAnonymousUser(any(UserAnonymousRequestDTO.class)))
                .thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<UserAnonymousResponseDTO>> response =
                this.userRestController.anonymousRegister(mock(UserAnonymousRequestDTO.class));

        // Assert
        this.assertSuccessfulResponse(response, expectedOutput);

        verify(this.userService, times(1))
                .createAnonymousUser(any(UserAnonymousRequestDTO.class));
        verifyNoInteractions(this.authenticationService);
    }

    @Test
    void anonymousRegister_shouldThrowRokaMokaContentDuplicatedException_whenUserAlreadyExistsByEmailOrName()
    throws RokaMokaContentDuplicatedException {
        // Arrange
        when(this.userService.createAnonymousUser(any(UserAnonymousRequestDTO.class)))
                .thenThrow(RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.userRestController.anonymousRegister(mock(UserAnonymousRequestDTO.class)));

        verify(this.userService, times(1))
                .createAnonymousUser(any(UserAnonymousRequestDTO.class));
        verifyNoInteractions(this.authenticationService);
    }

    //endregion

    //region login
    @Test
    void login_shouldReturnUserToken_whenSuccessful() {
        // Arrange
        UserAuthDTO expectedOutput = Instancio.create(UserAuthDTO.class);

        when(this.authenticationService.authenticate(any(Authentication.class)))
                .thenReturn(expectedOutput.jwt());

        // Act
        ResponseEntity<ApiResponseWrapper<UserAuthDTO>> response =
                this.userRestController.login(mock(Authentication.class));

        // Assert
        this.assertSuccessfulResponse(response, expectedOutput);

        verify(this.authenticationService, times(1))
                .authenticate(any(Authentication.class));
        verifyNoInteractions(this.userService);
    }
    //endregion

    //region teste
    @Test
    void teste_shouldReturnString_whenSuccessful() {
        // Arrange
        String expectedOutput = "Ok, funcionou";

        // Act
        ResponseEntity<ApiResponseWrapper<String>> response = this.userRestController.teste();

        // Assert
        this.assertSuccessfulResponse(response, expectedOutput);

        verifyNoInteractions(this.userService, this.authenticationService);
    }
    //endregion

    //region resetPassword
    @Test
    void resetPassword_shouldReturnVoid_whenSuccessful()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        UserResetPasswordDTO userDTO = Instancio.create(UserResetPasswordDTO.class);

        doNothing().when(this.userService).resetUserPassword(userDTO);

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response = this.userRestController.resetPassword(userDTO);

        // Assert
        this.assertSuccessfulResponse(response, null);

        verify(this.userService, times(1))
                .resetUserPassword(any(UserResetPasswordDTO.class));
        verifyNoInteractions(this.authenticationService);
    }

    @Test
    void resetPassword_shouldThrowRokaMokaContentNotFoundException_whenUserWasNotFound()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        doThrow(RokaMokaContentNotFoundException.class)
                .when(this.userService)
                .resetUserPassword(any(UserResetPasswordDTO.class));

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.userRestController.resetPassword(mock(UserResetPasswordDTO.class)));

        verify(this.userService, times(1)).resetUserPassword(any(UserResetPasswordDTO.class));
        verifyNoInteractions(this.authenticationService);
    }

    @Test
    void resetPassword_shouldThrowRokaMokaForbiddenException_whenUserIsNotAllowedToResetPassword()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        doThrow(RokaMokaForbiddenException.class)
                .when(this.userService)
                .resetUserPassword(any(UserResetPasswordDTO.class));

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class,
                () -> this.userRestController.resetPassword(mock(UserResetPasswordDTO.class)));

        verify(this.userService, times(1)).resetUserPassword(any(UserResetPasswordDTO.class));
        verifyNoInteractions(this.authenticationService);
    }
    //endregion

    //region getLoggedUserInformation
    @Test
    void getLoggedUserInformation_shouldReturnLoggedUserInformation_whenSuccessful()
    throws RokaMokaContentNotFoundException {
        // Arrange
        UserOutputDTO expectedOutput = Instancio.create(UserOutputDTO.class);

        when(this.userService.getLoggedUserInformation()).thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<UserOutputDTO>> response = this.userRestController.getLoggedUserInformation();

        // Assert
        this.assertSuccessfulResponse(response, expectedOutput);

        verify(this.userService, times(1)).getLoggedUserInformation();
        verifyNoInteractions(this.authenticationService);
    }

    @Test
    void getLoggedUserInformation_shouldThrowRokaMokaContentNotFoundException_whenLoggedUserInformationIsNotInContext()
    throws RokaMokaContentNotFoundException {
        // Arrange
        doThrow(RokaMokaContentNotFoundException.class).when(this.userService).getLoggedUserInformation();

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.userRestController.getLoggedUserInformation());

        verify(this.userService, times(1)).getLoggedUserInformation();
        verifyNoInteractions(this.authenticationService);
    }
    //endregion
}
