package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.authentication.input.AuthResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.authentication.output.AuthOutputDTO;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@code AuthenticationRestController} class, which is responsible for handling
 * authentication-related endpoints.
 *
 * @author MauricioMucci
 * @see AuthenticationRestController
 * @see AuthenticationService
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private AuthenticationRestController authenticationRestController;

    @Mock private AuthenticationService authenticationService;
    @Mock private IUserService userService;

    //region login
    @Test
    void login_shouldReturnUserToken_whenSuccessful() {
        // Arrange
        AuthOutputDTO expectedOutput = Instancio.create(AuthOutputDTO.class);

        when(this.authenticationService.authenticate(any(Authentication.class))).thenReturn(expectedOutput.jwt());

        // Act
        ResponseEntity<ApiResponseWrapper<AuthOutputDTO>> response = this.authenticationRestController.login(
                mock(Authentication.class));

        // Assert
        this.assertExpectedResponse(response, expectedOutput);

        verify(this.authenticationService).authenticate(any(Authentication.class));
    }
    //endregion

    //region resetPassword
    @Test
    void resetPassword_shouldReturnVoid_whenSuccessful() {
        // Arrange
        AuthResetPasswordDTO userDTO = Instancio.create(AuthResetPasswordDTO.class);

        doNothing().when(this.userService).resetUserPassword(userDTO);

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response = this.authenticationRestController.resetPassword(userDTO);

        // Assert
        this.assertVoidResponse(response);

        verify(this.userService).resetUserPassword(any(AuthResetPasswordDTO.class));
    }
    //endregion
}
