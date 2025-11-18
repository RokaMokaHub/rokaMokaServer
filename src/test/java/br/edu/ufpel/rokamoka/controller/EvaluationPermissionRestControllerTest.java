package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.permission.input.EvaluationPermissionDTO;
import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;
import br.edu.ufpel.rokamoka.service.evaluation.IEvaluationPermissionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EvaluationPermissionRestController} class, which is responsible for handling
 * permission-related endpoints.
 *
 * @author MauricioMucci
 * @see IEvaluationPermissionService
 */
@ExtendWith(MockitoExtension.class)
class EvaluationPermissionRestControllerTest implements ControllerResponseValidator {

    @InjectMocks private EvaluationPermissionRestController evaluationPermissionController;

    @Mock private IEvaluationPermissionService evaluationPermissionService;

    private EvaluationPermissionDTO evaluationDTO;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        this.evaluationDTO = mock(EvaluationPermissionDTO.class);
        this.authentication = mock(Authentication.class);
    }

    //region deny
    @Test
    void deny_shouldReturnVoid_whenSuccessful() {
        // Arrange
        when(this.evaluationDTO.justificativa()).thenReturn("");
        when(this.authentication.getName()).thenReturn("");

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response = this.evaluationPermissionController.deny(1L,
                this.evaluationDTO, this.authentication);

        // Assert
        this.assertVoidResponse(response);

        verify(this.evaluationPermissionService).deny(anyLong(), anyString(), anyString());
    }
    //endregion

    //region accept
    @Test
    void accept_shouldReturnVoid_whenSuccessful() {
        // Arrange
        when(this.authentication.getName()).thenReturn("");

        // Act
        ResponseEntity<ApiResponseWrapper<Void>> response = this.evaluationPermissionController.accept(1L,
                this.authentication);

        // Assert
        this.assertVoidResponse(response);

        verify(this.evaluationPermissionService).accept(anyLong(), anyString());
    }
    //endregion

    //region list
    static Stream<List<RequestDetailsDTO>> provideRequestDetailsDTOList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(RequestDetailsDTO.class).create());
    }

    @ParameterizedTest
    @MethodSource("provideRequestDetailsDTOList")
    void list_shouldReturnRequestDetailsList_whenCalled(List<RequestDetailsDTO> requests) {
        // Arrange
        when(this.evaluationPermissionService.findAllPendingRequest()).thenReturn(requests);

        // Act
        ResponseEntity<ApiResponseWrapper<List<RequestDetailsDTO>>> response =
                this.evaluationPermissionController.list();

        // Assert
        this.assertListResponse(response, requests);
        verify(this.evaluationPermissionService).findAllPendingRequest();
    }
    //endregion
}
