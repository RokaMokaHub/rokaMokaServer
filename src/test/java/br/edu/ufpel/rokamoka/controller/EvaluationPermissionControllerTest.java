package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.permission.input.EvaluationPermissionDTO;
import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.service.evaluation.IEvaluationPermissionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EvaluationPermissionController} class, which is responsible for handling permission-related
 * endpoints.
 *
 * @author MauricioMucci
 * @see IEvaluationPermissionService
 */
@ExtendWith(MockitoExtension.class)
class EvaluationPermissionControllerTest implements ControllerResponseValidator {

    @InjectMocks private EvaluationPermissionController evaluationPermissionController;

    @Mock private IEvaluationPermissionService evaluationPermissionService;

    //region deny
    @Test
    void deny_shouldReturnVoid_whenSuccessful() throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        EvaluationPermissionDTO evaluationDTO = mock(EvaluationPermissionDTO.class);
        Authentication authentication = mock(Authentication.class);

        when(evaluationDTO.justificativa()).thenReturn("");
        when(authentication.getName()).thenReturn("");

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response =
                this.evaluationPermissionController.deny(1L, evaluationDTO, authentication);

        // Assert
        this.assertVoidResponse(response);

        verify(this.evaluationPermissionService).deny(anyLong(), anyString(), anyString());
    }

    @Test
    void deny_shouldThrowRokaMokaContentNotFoundException_whenPermissionRequestDoesNotExistById()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        EvaluationPermissionDTO evaluationDTO = mock(EvaluationPermissionDTO.class);
        Authentication authentication = mock(Authentication.class);

        when(evaluationDTO.justificativa()).thenReturn("");
        when(authentication.getName()).thenReturn("");
        doThrow(RokaMokaContentNotFoundException.class).when(this.evaluationPermissionService)
                .deny(anyLong(), anyString(), anyString());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.evaluationPermissionController.deny(1L, evaluationDTO, authentication));

        verify(this.evaluationPermissionService).deny(anyLong(), anyString(), anyString());
    }

    @Test
    void deny_shouldThrowRokaMokaForbiddenException_whenRequestStatusIsNotPending()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        EvaluationPermissionDTO evaluationDTO = mock(EvaluationPermissionDTO.class);
        Authentication authentication = mock(Authentication.class);

        when(evaluationDTO.justificativa()).thenReturn("");
        when(authentication.getName()).thenReturn("");
        doThrow(RokaMokaForbiddenException.class).when(this.evaluationPermissionService)
                .deny(anyLong(), anyString(), anyString());

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class,
                () -> this.evaluationPermissionController.deny(1L, evaluationDTO, authentication));

        verify(this.evaluationPermissionService).deny(anyLong(), anyString(), anyString());
    }
    //endregion

    //region accept
    @Test
    void accept_shouldReturnVoid_whenSuccessful() throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("");

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response =
                this.evaluationPermissionController.accept(1L, authentication);

        // Assert
        this.assertVoidResponse(response);

        verify(this.evaluationPermissionService).accept(anyLong(), anyString());
    }

    @Test
    void accept_shouldThrowRokaMokaContentNotFoundException_whenPermissionRequestDoesNotExistById()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("");
        doThrow(RokaMokaContentNotFoundException.class).when(this.evaluationPermissionService)
                .accept(anyLong(), anyString());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.evaluationPermissionController.accept(1L, authentication));

        verify(this.evaluationPermissionService).accept(anyLong(), anyString());
    }

    @Test
    void accept_shouldThrowRokaMokaForbiddenException_whenRequestStatusIsNotPending()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("");
        doThrow(RokaMokaForbiddenException.class).when(this.evaluationPermissionService).accept(anyLong(), anyString());

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class,
                () -> this.evaluationPermissionController.accept(1L, authentication));

        verify(this.evaluationPermissionService).accept(anyLong(), anyString());
    }
    //endregion

    //region list
    static Stream<Arguments> buildListInput() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Instancio.ofList(RequestDetailsDTO.class).create())
        );
    }

    @ParameterizedTest
    @MethodSource("buildListInput")
    void list_shouldReturnRequestDetailsList_whenCalled(List<RequestDetailsDTO> requests) {
        // Arrange
        when(this.evaluationPermissionService.findAllPendingRequest()).thenReturn(requests);

        // Act
        ResponseEntity<ApiResponseWrapper<List<RequestDetailsDTO>>> response = this.evaluationPermissionController.list();

        // Assert
        verify(this.evaluationPermissionService).findAllPendingRequest();

        if (requests.isEmpty()) {
            this.assertEmptyListResponse(response);
            return;
        }
        this.assertListResponse(response);
    }
    //endregion
}
