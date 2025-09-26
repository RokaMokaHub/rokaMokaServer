package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.service.IRequestPermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void createPermissionRequestCurator() {
    }
    //endregion

    //region createPermissionRequestResearcher
    @Test
    void createPermissionRequestResearcher() {
    }
    //endregion

    //region getPermissionRequestStatus
    @Test
    void getPermissionRequestStatus() {
    }
    //endregion
}
