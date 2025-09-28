package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.IRequestPermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link RequestPermissionController} class, which is responsible for handling
 * permission-request-related endpoints.
 *
 * @author MauricioMucci
 * @see IRequestPermissionService
 */
@ExtendWith(MockitoExtension.class)
class RequestPermissionControllerTest implements ControllerResponseValidator {

    @InjectMocks private RequestPermissionController requestPermissionController;

    @Mock private IRequestPermissionService requestPermissionService;

    //region createPermissionRequestCurator
    @Test
    void createPermissionRequestCurator_shouldReturnPermissionRequestStatusDTO_whenSuccessful()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        String username = "username";
        Authentication authentication = mock(Authentication.class);
        PermissionRequestStatusDTO request = mock(PermissionRequestStatusDTO.class);

        when(authentication.getName()).thenReturn(username);
        when(this.requestPermissionService.createRequest(username, RoleEnum.CURATOR)).thenReturn(request);

        // Act
        ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> response =
                this.requestPermissionController.createPermissionRequestCurator(authentication);

        // Assert
        this.assertExpectedResponse(response, request);

        verify(this.requestPermissionService).createRequest(username, RoleEnum.CURATOR);
    }

    @Test
    void createPermissionRequestCurator_shouldThrowRokaMokaContentNotFoundException_whenAnyEntityIsNotFound()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        String username = "username";
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(username);
        when(this.requestPermissionService.createRequest(username, RoleEnum.CURATOR)).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionController.createPermissionRequestCurator(authentication));

        verify(this.requestPermissionService).createRequest(username, RoleEnum.CURATOR);
    }

    @Test
    void createPermissionRequestCurator_shouldThrowRokaMokaContentNotFoundException_whenRequestAlreadyExists()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        String username = "username";
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(username);
        when(this.requestPermissionService.createRequest(username, RoleEnum.CURATOR)).thenThrow(
                RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.requestPermissionController.createPermissionRequestCurator(authentication));

        verify(this.requestPermissionService).createRequest(username, RoleEnum.CURATOR);
    }
    //endregion

    //region createPermissionRequestResearcher
    @Test
    void createPermissionRequestResearcher_shouldReturnPermissionRequestStatusDTO_whenSuccessful()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        String username = "username";
        Authentication authentication = mock(Authentication.class);
        PermissionRequestStatusDTO request = mock(PermissionRequestStatusDTO.class);

        when(authentication.getName()).thenReturn(username);
        when(this.requestPermissionService.createRequest(username, RoleEnum.RESEARCHER)).thenReturn(request);

        // Act
        ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> response =
                this.requestPermissionController.createPermissionRequestResearcher(authentication);

        // Assert
        this.assertExpectedResponse(response, request);

        verify(this.requestPermissionService).createRequest(username, RoleEnum.RESEARCHER);
    }

    @Test
    void createPermissionRequestResearcher_shouldThrowRokaMokaContentNotFoundException_whenAnyEntityIsNotFound()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        String username = "username";
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(username);
        when(this.requestPermissionService.createRequest(username, RoleEnum.RESEARCHER)).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionController.createPermissionRequestResearcher(authentication));

        verify(this.requestPermissionService).createRequest(username, RoleEnum.RESEARCHER);
    }

    @Test
    void createPermissionRequestResearcher_shouldThrowRokaMokaContentNotFoundException_whenRequestAlreadyExists()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        String username = "username";
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(username);
        when(this.requestPermissionService.createRequest(username, RoleEnum.RESEARCHER)).thenThrow(
                RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.requestPermissionController.createPermissionRequestResearcher(authentication));

        verify(this.requestPermissionService).createRequest(username, RoleEnum.RESEARCHER);
    }
    //endregion

    //region getPermissionRequestStatus
    @Test
    void getPermissionRequestStatus_shouldReturnPermissionRequestStatusDTO_whenSuccessful()
    throws RokaMokaContentNotFoundException {
        // Arrange
        PermissionRequestStatusDTO request = mock(PermissionRequestStatusDTO.class);

        when(this.requestPermissionService.getPermissionRequestStatus(anyLong())).thenReturn(request);

        // Act
        ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> response =
                this.requestPermissionController.getPermissionRequestStatus(anyLong());

        // Assert
        this.assertExpectedResponse(response, request);

        verify(this.requestPermissionService).getPermissionRequestStatus(anyLong());
    }

    @Test
    void getPermissionRequestStatus_shouldThrowRokaMokaContentNotFoundException_whenAnyEntityIsNotFound()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.requestPermissionService.getPermissionRequestStatus(anyLong())).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionController.getPermissionRequestStatus(1L));

        verify(this.requestPermissionService).getPermissionRequestStatus(anyLong());
    }
    //endregion
}
