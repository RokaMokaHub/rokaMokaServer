package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.PermissionReq;
import br.edu.ufpel.rokamoka.core.RequestStatus;
import br.edu.ufpel.rokamoka.core.Role;
import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.PermissionReqRepository;
import br.edu.ufpel.rokamoka.repository.RoleRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link RequestPermissionService} class, which is responsible for handling
 * permission-request-related API operations.
 *
 * @author MauricioMucci
 * @see PermissionReqRepository
 * @see RoleRepository
 * @see IUserService
 */
@ExtendWith(MockitoExtension.class)
class RequestPermissionServiceTest implements MockRepository<PermissionReq> {

    @InjectMocks private RequestPermissionService requestPermissionService;

    @Mock private PermissionReqRepository permissionReqRepository;
    @Mock private RoleRepository roleRepository;

    @Mock private IUserService userService;

    @Captor private ArgumentCaptor<PermissionReq> requestCaptor;

    private User requester;
    private Role targetRole;

    @BeforeEach
    void setUp() {
        this.requester = mock(User.class);
        this.targetRole = mock(Role.class);
    }

    //region createRequest
    @ParameterizedTest
    @EnumSource(value = RoleEnum.class)
    void createRequest_shouldSaveNewRequest_whenSuccessful(RoleEnum roleName)
    throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        // Arrange
        when(this.userService.getByNome(anyString())).thenReturn(this.requester);
        when(this.roleRepository.findByName(roleName)).thenReturn(this.targetRole);
        when(this.permissionReqRepository.existsByRequesterAndStatusAndTargetRole(this.requester, RequestStatus.PENDING,
                this.targetRole)).thenReturn(false);
        when(this.permissionReqRepository.save(any(PermissionReq.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        PermissionRequestStatusDTO actual = this.requestPermissionService.createRequest("", roleName);

        // Assert
        verify(this.userService).getByNome(anyString());
        verify(this.roleRepository).findByName(roleName);
        verify(this.permissionReqRepository).existsByRequesterAndStatusAndTargetRole(this.requester,
                RequestStatus.PENDING, this.targetRole);
        verify(this.permissionReqRepository).save(this.requestCaptor.capture());

        PermissionReq newRequest = this.requestCaptor.getValue();
        assertOutputByPermissionReq(newRequest, actual);
    }

    private static void assertOutputByPermissionReq(PermissionReq expected, PermissionRequestStatusDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getStatus(), actual.status());
        assertEquals(expected.getTargetRole().getName(), actual.targetRole());
    }

    @ParameterizedTest
    @EnumSource(value = RoleEnum.class)
    void createRequest_shouldThrowRokaMokaContentDuplicatedException_whenRequestAlreadyExists(RoleEnum roleName)
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.userService.getByNome(anyString())).thenReturn(this.requester);
        when(this.roleRepository.findByName(roleName)).thenReturn(this.targetRole);
        when(this.permissionReqRepository.existsByRequesterAndStatusAndTargetRole(this.requester, RequestStatus.PENDING,
                this.targetRole)).thenReturn(true);

        // Act & Assert
        assertThrows(RokaMokaContentDuplicatedException.class,
                () -> this.requestPermissionService.createRequest("", roleName));

        verify(this.userService).getByNome(anyString());
        verify(this.roleRepository).findByName(roleName);
        verify(this.permissionReqRepository).existsByRequesterAndStatusAndTargetRole(this.requester, RequestStatus.PENDING,
                this.targetRole);
        verifyNoMoreInteractions(this.permissionReqRepository);
    }
    //endregion

    //region getPermissionRequestStatus
    @Test
    void getPermissionRequestStatus_shouldReturnPermissionRequestStatusDTO_whenRequestExistsById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        PermissionReq request = mock(PermissionReq.class);

        when(this.permissionReqRepository.findById(anyLong())).thenReturn(Optional.of(request));

        // Act
        PermissionRequestStatusDTO actual = this.requestPermissionService.getPermissionRequestStatus();

        // Assert
        assertOutputByPermissionReq(request, actual);

        verify(this.permissionReqRepository).findById(anyLong());
    }

    @Test
    void getPermissionRequestStatus_shouldThrowRokaMokaContentNotFoundException_whenRequestDoesNotExistById() {
        // Arrange
        when(this.permissionReqRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionService.getPermissionRequestStatus());

        verify(this.permissionReqRepository).findById(anyLong());
    }
    //endregion
}
