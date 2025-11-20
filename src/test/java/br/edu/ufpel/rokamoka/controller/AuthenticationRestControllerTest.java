package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.authentication.output.AuthOutputDTO;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author MauricioMucci
 */
@ExtendWith(MockitoExtension.class)
class AuthenticationRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private AuthenticationRestController authenticationRestController;

    @Mock private AuthenticationService authenticationService;

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

}
