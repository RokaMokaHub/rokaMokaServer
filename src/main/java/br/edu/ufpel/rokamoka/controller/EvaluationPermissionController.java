package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.permission.input.EvaluationPermissionDTO;
import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.service.evaluation.IEvaluationPermissionService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Solicitação de Permissão",
        description = "API para aceitar ou rejeitar permissão, precisa ter perfil de curador ou admin")
@RestController
@RequestMapping("/evaluation/permission")
@RequiredArgsConstructor
public class EvaluationPermissionController extends RokaMokaController {

    private final IEvaluationPermissionService evaluationPermissionService;

    @PostMapping("/deny/{permissionId}")
    public ResponseEntity<ApiResponseWrapper<Void>> deny(@PathVariable Long permissionId,
            @RequestBody EvaluationPermissionDTO evaluationDTO, Authentication authentication)
    throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        this.evaluationPermissionService.deny(permissionId, evaluationDTO.justificativa(), authentication.getName());
        return this.success();
    }

    @PostMapping("accept/{permissionId}")
    public ResponseEntity<ApiResponseWrapper<Void>> accept(@PathVariable Long permissionId,
            Authentication authentication) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        this.evaluationPermissionService.accept(permissionId, authentication.getName());
        return this.success();
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseWrapper<List<RequestDetailsDTO>>> list() {
        List<RequestDetailsDTO> requests = this.evaluationPermissionService.findAllPedingRequest();
        return this.success(requests);
    }
}
