package br.edu.ufpel.rokamoka.service.evaluation;

import br.edu.ufpel.rokamoka.repository.PermissionRegRepository;
import br.edu.ufpel.rokamoka.repository.PermissionReqRepository;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    //region deny
    @Test
    void deny() {
    }
    //endregion

    //region accept
    @Test
    void accept() {
    }
    //endregion

    //region findAllPedingRequest
    @Test
    void findAllPedingRequest() {
    }
    //endregion
}
