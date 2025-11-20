package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.authentication.input.AuthResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.authentication.output.AuthOutputDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserInputDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserOutputDTO;
import br.edu.ufpel.rokamoka.service.user.UserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@code UserRestController} class, which is responsible for handling user-related endpoints.
 *
 * @author MauricioMucci
 * @see UserRestController
 * @see UserService
 */
@ExtendWith(MockitoExtension.class)
class UserRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private UserRestController userRestController;

    @Mock private UserService userService;

    //region register
    @Test
    void register_shouldReturnTokenGeneratedForNewUser_whenSuccessful() {
        // Arrange
        AuthOutputDTO expectedOutput = Instancio.create(AuthOutputDTO.class);

        when(this.userService.createNormalUser(any(UserInputDTO.class))).thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<AuthOutputDTO>> response = this.userRestController.register(
                mock(UserInputDTO.class));

        // Assert
        this.assertExpectedResponse(response, expectedOutput);

        verify(this.userService).createNormalUser(any(UserInputDTO.class));
    }
    //endregion

    //region anonymousRegister
    @Test
    void anonymousRegister_shouldReturnAnonymousToken_whenSuccessful() {
        // Arrange
        UserAnonymousResponseDTO expectedOutput = Instancio.create(UserAnonymousResponseDTO.class);

        when(this.userService.createAnonymousUser(any(UserAnonymousRequestDTO.class))).thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<UserAnonymousResponseDTO>> response =
                this.userRestController.anonymousRegister(
                mock(UserAnonymousRequestDTO.class));

        // Assert
        this.assertExpectedResponse(response, expectedOutput);

        verify(this.userService).createAnonymousUser(any(UserAnonymousRequestDTO.class));
    }
    //endregion

    //region resetPassword
    @Test
    void resetPassword_shouldReturnVoid_whenSuccessful() {
        // Arrange
        AuthResetPasswordDTO userDTO = Instancio.create(AuthResetPasswordDTO.class);

        doNothing().when(this.userService).resetUserPassword(userDTO);

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response = this.userRestController.resetPassword(userDTO);

        // Assert
        this.assertVoidResponse(response);

        verify(this.userService).resetUserPassword(any(AuthResetPasswordDTO.class));
    }
    //endregion

    //region getLoggedUserInformation
    @Test
    void getLoggedUserInformation_shouldReturnLoggedUserInformation_whenSuccessful() {
        // Arrange
        UserOutputDTO expectedOutput = Instancio.create(UserOutputDTO.class);

        when(this.userService.getLoggedUserInformation()).thenReturn(expectedOutput);

        // Act
        ResponseEntity<ApiResponseWrapper<UserOutputDTO>> response = this.userRestController.getLoggedUserInformation();

        // Assert
        this.assertExpectedResponse(response, expectedOutput);

        verify(this.userService).getLoggedUserInformation();
    }
    //endregion
}
