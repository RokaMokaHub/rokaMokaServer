package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.service.evaluation.IEvaluationPermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link EvaluationPermissionController} class, which is responsible for handling
 * permission-related endpoints.
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
    void deny() {
    }
    //endregion

    //region accept
    @Test
    void accept() {
    }
    //endregion

    //region list
    @Test
    void list() {
    }
    //endregion
}
