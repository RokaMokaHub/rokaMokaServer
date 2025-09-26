package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.PermissionReq;
import br.edu.ufpel.rokamoka.repository.PermissionReqRepository;
import br.edu.ufpel.rokamoka.repository.RoleRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    //region createRequest
    @Test
    void createRequest() {
    }
    //endregion

    //region getPermissionRequestStatus
    @Test
    void getPermissionRequestStatus() {
    }
    //endregion
}
