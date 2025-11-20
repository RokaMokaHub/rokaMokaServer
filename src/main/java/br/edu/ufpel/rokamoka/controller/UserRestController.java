package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.authentication.input.AuthResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.authentication.output.AuthOutputDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserInputDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserOutputDTO;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller providing endpoints for CRUD operations on the {@code User} resource.
 *
 * @author iyisakuma
 * @see IUserService
 */
@Validated
@RequiredArgsConstructor
@Tag(name = "Usuário", description = "API para cadastros de usuários")
@RestController
@RequestMapping("/user")
public class UserRestController extends RokaMokaController {

    private final IUserService userService;

    /**
     * Creates a new user using the provided data.
     *
     * @param userDTO A {@link UserInputDTO} containing the user's name, email and password.
     *
     * @return A {@link ResponseEntity} containing the created user's JWT
     */
    @Operation(summary = "Criação de usuário \"normal\"",
            description = "Cria usuário normal, isto é, aqueles com email e senha.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Usuário criado")})
    @PostMapping(value = "/normal/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<AuthOutputDTO>> register(@RequestBody @Valid UserInputDTO userDTO) {
        AuthOutputDTO normalUser = this.userService.createNormalUser(userDTO);
        return this.success(normalUser);
    }

    @Operation(summary = "Criação de usuário anônimo", description = "Cria usuário anônimo")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Usuário criado")})
    @PostMapping(value = "/anonymous/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserAnonymousResponseDTO>> anonymousRegister(
            @RequestBody @Valid UserAnonymousRequestDTO userDTO) {
        UserAnonymousResponseDTO anonymousUser = this.userService.createAnonymousUser(userDTO);
        return this.success(anonymousUser);
    }

    /**
     * Resets the password of a user using the provided credentials.
     *
     * @param userDTO A {@link AuthResetPasswordDTO} containing the user's credentials.
     *
     * @return A {@link ResponseEntity} wrapping an {@code ApiResponseWrapper<Void>} indicating this.success or failure.
     */
    @Operation(summary = "Redefinição de senha do usuário",
            description = "Permite que um usuário redefina sua senha fornecendo suas credenciais")
    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<Void>> resetPassword(@RequestBody @Valid AuthResetPasswordDTO userDTO) {
        this.userService.resetUserPassword(userDTO);
        return this.success();
    }

    /**
     * Retrieves information about the currently logged-in user.
     *
     * @return A {@link ResponseEntity} wrapping an {@link ApiResponseWrapper}<{@link UserOutputDTO}>
     */
    @Operation(summary = "Visualizar dados do usuário",
            description = "Retorna alguns dados do usuário logado como: nome, email, perfil e mokadex")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserOutputDTO>> getLoggedUserInformation() {
        UserOutputDTO loggedUserInformation = this.userService.getLoggedUserInformation();
        return this.success(loggedUserInformation);
    }
}
