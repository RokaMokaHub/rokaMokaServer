package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;
import br.edu.ufpel.rokamoka.service.IRequestPermissionService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@Tag(name = "Solicitação de Acesso",
        description = "API para o usuário solicitar diferentes níveis de acesso (e.g., Curador ou Pesquisador)")
@RestController
@RequestMapping("/request/permission")
public class RequestPermissionRestController extends RokaMokaController {

    private final IRequestPermissionService requestPermissionService;

    @Operation(summary = "Solicitar acesso como curador")
    @PostMapping(value = "/curator", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> createPermissionRequestCurator(
            Authentication authentication) {
        PermissionRequestStatusDTO request = this.requestPermissionService.createRequest(authentication.getName(),
                RoleEnum.CURATOR);
        return this.success(request);
    }

    @Operation(summary = "Solicitar acesso como Pesquisador")
    @PostMapping(value = "/researcher", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<PermissionRequestStatusDTO>> createPermissionRequestResearcher(
            Authentication authentication) {
        PermissionRequestStatusDTO request = this.requestPermissionService.createRequest(authentication.getName(),
                RoleEnum.RESEARCHER);
        return this.success(request);
    }

    @Operation(summary = "Adquirir solicitações do usuário logado")
    @GetMapping(value = "/me/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<List<PermissionRequestStatusDTO>>> listAllPermissionRequestStatusByLoggedUser() {
        List<PermissionRequestStatusDTO> requests = this.requestPermissionService.getAllPermissionRequestStatusByLoggedUser();
        return this.success(requests);
    }
}
