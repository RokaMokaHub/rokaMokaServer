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
import org.instancio.Instancio;
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

    //region createRequest
    @ParameterizedTest
    @EnumSource(value = RoleEnum.class)
    void createRequest_shouldSaveNewRequest_whenSuccessful(RoleEnum roleName)
    throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        // Arrange
        User requester = mock(User.class);
        Role targetRole = mock(Role.class);

        when(this.userService.getByNome(anyString())).thenReturn(requester);
        when(this.roleRepository.findByName(roleName)).thenReturn(targetRole);
        when(this.permissionReqRepository.existsByRequesterAndStatusAndTargetRole(requester, RequestStatus.PENDING,
                targetRole)).thenReturn(false);
        when(this.permissionReqRepository.save(any(PermissionReq.class))).thenAnswer(
                inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        PermissionRequestStatusDTO actual = this.requestPermissionService.createRequest("", roleName);

        // Assert
        verify(this.permissionReqRepository).save(this.requestCaptor.capture());

        PermissionReq newRequest = this.requestCaptor.getValue();
        this.assertOutputByPermissionReq(newRequest, actual);
    }

    private void assertOutputByPermissionReq(PermissionReq expected, PermissionRequestStatusDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.id());
        assertEquals(expected.getStatus(), actual.status());
        assertEquals(expected.getTargetRole().getName(), actual.targetRole());
    }
    //endregion

    //region getPermissionRequestStatus
    @Test
    void getPermissionRequestStatus_shouldReturnPermissionRequestStatusDTO_whenRequestExistsById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        PermissionReq foundById = Instancio.create(PermissionReq.class);

        when(this.permissionReqRepository.findById(anyLong())).thenReturn(Optional.of(foundById));

        // Act
        PermissionRequestStatusDTO actual = this.requestPermissionService.getPermissionRequestStatus(foundById.getId());

        // Assert
        this.assertOutputByPermissionReq(foundById, actual);

        verify(this.permissionReqRepository).findById(anyLong());
    }

    @Test
    void getPermissionRequestStatus_shouldThrowRokaMokaContentNotFoundException_whenRequestDoesNotExistById() {
        // Arrange
        when(this.permissionReqRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionService.getPermissionRequestStatus(1L));

        verify(this.permissionReqRepository).findById(anyLong());
    }
    //endregion
}
