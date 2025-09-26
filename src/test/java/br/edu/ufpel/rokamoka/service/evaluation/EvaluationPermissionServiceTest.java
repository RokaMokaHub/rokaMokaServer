package br.edu.ufpel.rokamoka.service.evaluation;

import br.edu.ufpel.rokamoka.core.PermissionReg;
import br.edu.ufpel.rokamoka.core.PermissionReq;
import br.edu.ufpel.rokamoka.core.RequestStatus;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.PermissionRegRepository;
import br.edu.ufpel.rokamoka.repository.PermissionReqRepository;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EvaluationPermissionService} class, which is responsible for handling permission-related
 * API operations.
 *
 * @author MauricioMucci
 * @see PermissionRegRepository
 * @see PermissionReqRepository
 * @see IUserService
 */
@ExtendWith(MockitoExtension.class)
class EvaluationPermissionServiceTest {

    @InjectMocks private EvaluationPermissionService evaluationPermissionService;

    @Mock private PermissionRegRepository registerRepository;
    @Mock private PermissionReqRepository requestRepository;
    @Mock private IUserService userService;

    @Captor private ArgumentCaptor<PermissionReg> registerCaptor;

    //region deny
    @Test
    void deny_shouldFlipStatusToDenyAndSaveNewRegister_whenSuccessful()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        PermissionReq permissionReq = new PermissionReq();
        permissionReq.setStatus(RequestStatus.PENDING);
        PermissionReq spyRequest = spy(permissionReq);

        User reviewer = mock(User.class);

        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.of(spyRequest));
        when(this.requestRepository.save(any(PermissionReq.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(this.userService.getByNome(anyString())).thenReturn(reviewer);
        when(this.registerRepository.save(any(PermissionReg.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        // Act
        this.evaluationPermissionService.deny(1L, "", "");

        // Assert
        verify(this.requestRepository).findById(anyLong());
        verify(this.requestRepository).save(any(PermissionReq.class));
        verify(this.userService).getByNome(anyString());
        verify(this.registerRepository).save(this.registerCaptor.capture());

        this.assertPermissionReq(spyRequest, RequestStatus.DENY);
        PermissionReg register = this.registerCaptor.getValue();
        this.assertPermissionRegister(register, reviewer, spyRequest);
    }

    private void assertPermissionReq(PermissionReq request, RequestStatus status) {
        assertNotNull(request);
        assertEquals(status, request.getStatus());
    }

    private void assertPermissionRegister(PermissionReg register, User reviewer, PermissionReq request) {
        assertNotNull(register);
        assertEquals("", register.getJustification());
        assertEquals(reviewer, register.getReviewer());
        assertEquals(request, register.getRequest());
    }

    @Test
    void deny_shouldThrowRokaMokaContentNotFoundException_whenRequestDoesNotExistById() {
        // Arrange
        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.evaluationPermissionService.deny(1L, "", ""));

        verify(this.requestRepository).findById(anyLong());
    }

    @Test
    void deny_shouldThrowRokaMokaContentNotFoundException_whenUserDoesNotExistByNome()
    throws RokaMokaContentNotFoundException {
        // Arrange
        PermissionReq permissionReq = mock(PermissionReq.class);

        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.of(permissionReq));
        when(permissionReq.isPending()).thenReturn(true);
        when(this.userService.getByNome(anyString())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.evaluationPermissionService.deny(1L, "", ""));

        verify(this.requestRepository).findById(anyLong());
    }

    @Test
    void deny_shouldThrowRokaMokaForbiddenException_whenRequestStatusIsNotPending() {
        // Arrange
        PermissionReq permissionReq = mock(PermissionReq.class);

        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.of(permissionReq));
        when(permissionReq.isPending()).thenReturn(false);

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class, () -> this.evaluationPermissionService.deny(1L, "", ""));

        verify(this.requestRepository).findById(anyLong());
    }
    //endregion

    //region accept
    @Test
    void accept_FlipStatusToConfirmAndSaveNewRegisterAndUpdateUserRole_whenSuccessful()
    throws RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        // Arrange
        PermissionReq permissionReq = new PermissionReq();
        permissionReq.setStatus(RequestStatus.PENDING);
        PermissionReq spyRequest = spy(permissionReq);

        User reviewer = mock(User.class);

        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.of(spyRequest));
        when(this.requestRepository.save(any(PermissionReq.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(this.userService.getByNome(anyString())).thenReturn(reviewer);
        when(this.registerRepository.save(any(PermissionReg.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

        // Act
        this.evaluationPermissionService.accept(1L, "");

        // Assert
        verify(this.requestRepository).findById(anyLong());
        verify(this.requestRepository).save(any(PermissionReq.class));
        verify(this.userService).getByNome(anyString());
        verify(this.registerRepository).save(this.registerCaptor.capture());

        this.assertPermissionReq(spyRequest, RequestStatus.CONFIRM);
        PermissionReg register = this.registerCaptor.getValue();
        this.assertPermissionRegister(register, reviewer, spyRequest);
    }

    @Test
    void accept_shouldThrowRokaMokaContentNotFoundException_whenRequestDoesNotExistById() {
        // Arrange
        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.evaluationPermissionService.accept(1L, ""));

        verify(this.requestRepository).findById(anyLong());
        verifyNoInteractions(this.userService);
    }

    @Test
    void accept_shouldThrowRokaMokaContentNotFoundException_whenUserDoesNotExistByNome()
    throws RokaMokaContentNotFoundException {
        // Arrange
        PermissionReq permissionReq = mock(PermissionReq.class);

        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.of(permissionReq));
        when(permissionReq.isPending()).thenReturn(true);
        when(this.userService.getByNome(anyString())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.evaluationPermissionService.accept(1L, ""));

        verify(this.requestRepository).findById(anyLong());
    }

    @Test
    void accept_shouldThrowRokaMokaForbiddenException_whenRequestStatusIsNotPending() {
        // Arrange
        PermissionReq permissionReq = mock(PermissionReq.class);

        when(this.requestRepository.findById(anyLong())).thenReturn(Optional.of(permissionReq));
        when(permissionReq.isPending()).thenReturn(false);

        // Act & Assert
        assertThrows(RokaMokaForbiddenException.class, () -> this.evaluationPermissionService.accept(1L, ""));

        verify(this.requestRepository).findById(anyLong());
        verifyNoInteractions(this.userService);
    }
    //endregion

    //region findAllPendingRequest
    static Stream<Arguments> buildFindAllPendingRequestInput() {
        List<RequestDetailsDTO> pendingRequests = Instancio.ofList(RequestDetailsDTO.class).create();
        return Stream.of(Arguments.of(Collections.emptyList()), Arguments.of(pendingRequests));
    }

    @ParameterizedTest
    @MethodSource("buildFindAllPendingRequestInput")
    void findAllPendingRequest(List<RequestDetailsDTO> pendingRequests) {
        // Arrange
        when(this.requestRepository.findAllPendingRequestDetailed()).thenReturn(pendingRequests);

        // Act
        List<RequestDetailsDTO> actualOutputList = this.evaluationPermissionService.findAllPendingRequest();

        // Assert
        assertNotNull(actualOutputList);
        assertEquals(pendingRequests.size(), actualOutputList.size());

        verify(this.requestRepository).findAllPendingRequestDetailed();
    }
    //endregion
}
