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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

    //region getAllPermissionRequestStatusByLoggedUser
    static Stream<List<PermissionReq>> providePermissionReqList() {
        return Stream.of(Collections.emptyList(), Instancio.ofList(PermissionReq.class).create());
    }

    @ParameterizedTest
    @MethodSource("providePermissionReqList")
    void getAllPermissionRequestStatusByLoggedUser_shouldReturnAllPermissionRequestStatusDTO_whenRequestExistsByIdByLoggedInUser(
            List<PermissionReq> requests)
    throws RokaMokaContentNotFoundException {
        // Arrange
        User requester = mock(User.class);

        when(this.userService.getLoggedUser()).thenReturn(requester);
        when(this.permissionReqRepository.findByRequester(requester)).thenReturn(requests);

        // Act
        List<PermissionRequestStatusDTO> actual = this.requestPermissionService.getAllPermissionRequestStatusByLoggedUser();

        // Assert
        assertNotNull(actual);
        assertEquals(requests.size(), actual.size());

        verify(this.userService).getLoggedUser();
        verify(this.permissionReqRepository).findByRequester(requester);
    }

    @Test
    void getAllPermissionRequestStatusByLoggedUser_shouldThrowRokaMokaContentNotFoundException_whenLoggedUserIsNotFound()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.userService.getLoggedUser()).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class,
                () -> this.requestPermissionService.getAllPermissionRequestStatusByLoggedUser());

        verify(this.userService).getLoggedUser();
        verifyNoInteractions(this.permissionReqRepository);
    }
    //endregion
}
