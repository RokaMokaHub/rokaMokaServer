package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.IRequestPermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private Authentication authentication;
    private PermissionRequestStatusDTO request;

    @BeforeEach
    void setUp() {
        this.authentication = mock(Authentication.class);
        this.request = mock(PermissionRequestStatusDTO.class);
    }

    //region createPermissionRequestCurator
    @Test
    void createPermissionRequestCurator_shouldReturnPermissionRequestStatusDTO_whenSuccessful()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        when(this.authentication.getName()).thenReturn("");
        when(this.requestPermissionService.createRequest("", RoleEnum.CURATOR)).thenReturn(this.request);

        // Act
        ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> response =
                this.requestPermissionController.createPermissionRequestCurator(this.authentication);

        // Assert
        this.assertExpectedResponse(response, this.request);

        verify(this.requestPermissionService).createRequest("", RoleEnum.CURATOR);
    }

    @Test
    void createPermissionRequestCurator_shouldThrowRokaMokaContentNotFoundException_whenAnyEntityIsNotFound()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        when(this.authentication.getName()).thenReturn("");
        when(this.requestPermissionService.createRequest("", RoleEnum.CURATOR)).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionController.createPermissionRequestCurator(this.authentication));

        verify(this.requestPermissionService).createRequest("", RoleEnum.CURATOR);
    }

    @Test
    void createPermissionRequestCurator_shouldThrowRokaMokaContentNotFoundException_whenRequestAlreadyExists()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        when(this.authentication.getName()).thenReturn("");
        when(this.requestPermissionService.createRequest("", RoleEnum.CURATOR)).thenThrow(
                RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.requestPermissionController.createPermissionRequestCurator(this.authentication));

        verify(this.requestPermissionService).createRequest("", RoleEnum.CURATOR);
    }
    //endregion

    //region createPermissionRequestResearcher
    @Test
    void createPermissionRequestResearcher_shouldReturnPermissionRequestStatusDTO_whenSuccessful()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        when(this.authentication.getName()).thenReturn("");
        when(this.requestPermissionService.createRequest("", RoleEnum.RESEARCHER)).thenReturn(this.request);

        // Act
        ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> response =
                this.requestPermissionController.createPermissionRequestResearcher(this.authentication);

        // Assert
        this.assertExpectedResponse(response, this.request);

        verify(this.requestPermissionService).createRequest("", RoleEnum.RESEARCHER);
    }

    @Test
    void createPermissionRequestResearcher_shouldThrowRokaMokaContentNotFoundException_whenAnyEntityIsNotFound()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        when(this.authentication.getName()).thenReturn("");
        when(this.requestPermissionService.createRequest("", RoleEnum.RESEARCHER)).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionController.createPermissionRequestResearcher(this.authentication));

        verify(this.requestPermissionService).createRequest("", RoleEnum.RESEARCHER);
    }

    @Test
    void createPermissionRequestResearcher_shouldThrowRokaMokaContentNotFoundException_whenRequestAlreadyExists()
    throws RokaMokaContentDuplicatedException, RokaMokaContentNotFoundException {
        // Arrange
        when(this.authentication.getName()).thenReturn("");
        when(this.requestPermissionService.createRequest("", RoleEnum.RESEARCHER)).thenThrow(
                RokaMokaContentDuplicatedException.class);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.requestPermissionController.createPermissionRequestResearcher(this.authentication));

        verify(this.requestPermissionService).createRequest("", RoleEnum.RESEARCHER);
    }
    //endregion

    //region getPermissionRequestStatus
    @Test
    void getPermissionRequestStatus_shouldReturnPermissionRequestStatusDTO_whenSuccessful()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.requestPermissionService.getPermissionRequestStatus()).thenReturn(this.request);

        // Act
        ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> response =
                this.requestPermissionController.getPermissionRequestStatus();

        // Assert
        this.assertExpectedResponse(response, this.request);

        verify(this.requestPermissionService).getPermissionRequestStatus();
    }

    @Test
    void getPermissionRequestStatus_shouldThrowRokaMokaContentNotFoundException_whenAnyEntityIsNotFound()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.requestPermissionService.getPermissionRequestStatus()).thenThrow(
                RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionController.getPermissionRequestStatus());

        verify(this.requestPermissionService).getPermissionRequestStatus();
    }
    //endregion
}
