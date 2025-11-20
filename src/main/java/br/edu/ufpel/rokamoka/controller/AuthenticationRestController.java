package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.authentication.input.AuthResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.authentication.output.AuthOutputDTO;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller providing endpoints for authentication operations.
 *
 * @author MauricioMucci
 * @see RokaMokaController
 * @see AuthenticationService
 */
@Validated
@Tag(name = "Autenticação", description = "API para operações de autenticação")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
class AuthenticationRestController extends RokaMokaController {

    private final AuthenticationService authenticationService;
    private final IUserService userService;

    /**
     * Authenticates a user and generates a JWT.
     *
     * @param authentication An {@link Authentication} object containing the user's credentials.
     *
     * @return A {@link ResponseEntity} containing the user's JWT.
     */
    @Operation(security = @SecurityRequirement(name = "basic"), summary = "Login de um determinado usuário",
            description = "Login de um determinado usuário")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Login de usuário")})
    @GetMapping(value = "/login", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<AuthOutputDTO>> login(Authentication authentication) {
        String jwt = this.authenticationService.authenticate(authentication);
        return this.success(new AuthOutputDTO(jwt));
    }

    /**
     * Resets the password of a user using the provided credentials.
     *
     * @param userDTO A {@code AuthResetPasswordDTO} containing the user's credentials.
     *
     * @return A {@code ResponseEntity} wrapping an {@code ApiResponseWrapper<Void>} indicating success or failure.
     * @see AuthResetPasswordDTO
     */
    @Operation(summary = "Redefinição de senha do usuário",
            description = "Permite que um usuário redefina sua senha fornecendo suas credenciais")
    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<Void>> resetPassword(@RequestBody @Valid AuthResetPasswordDTO userDTO) {
        this.userService.resetUserPassword(userDTO);
        return this.success();
    }
}
