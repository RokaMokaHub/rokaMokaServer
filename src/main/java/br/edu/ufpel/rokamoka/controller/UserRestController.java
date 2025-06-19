package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousRequestDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserResetPasswordDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAuthDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * This class is a controller for user operations. It provides endpoints
 * for creating users and logging in.
 *
 * @author iyisakuma
 */
@Tag(name = "Usuário", description = "API para cadastros de usuários e login")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController extends RokaMokaController {

    private final IUserService IUserService;
    private final AuthenticationService authenticationService;

    /**
     * Creates a new user using the provided data.
     *
     * @param userDTO A {@link UserBasicDTO} containing the user's name, email and password.
     * @return A {@link ResponseEntity} containing the created user's JWT
     * @throws RokaMokaContentDuplicatedException if the email or name are already in use.
     */
    @Operation(summary = "Criação de usuário \"normal\"", description = "Cria usuário normal, isto é, aqueles com email e senha.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuário criado") })
    @PostMapping(value = "/normal/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserAuthDTO>> register(@RequestBody UserBasicDTO userDTO)
            throws RokaMokaContentDuplicatedException {
        return success(IUserService.createNormalUser(userDTO));
    }

    @Operation(summary = "Criação de usuário anônimo", description = "Cria usuário anônimo")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuário criado") })
    @PostMapping(value = "/anonymous/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserAnonymousResponseDTO>> anonymousRegister(
            @RequestBody UserAnonymousRequestDTO userDTO)
            throws RokaMokaContentDuplicatedException {
        return success(this.IUserService.createAnonymousUser(userDTO));
    }

    /**
     * Authenticates a user and generates a JWT.
     *
     * @param authentication An {@link Authentication} object containing the user's credentials.
     * @return A {@link ResponseEntity} containing the user's JWT.
     */
    @Operation(security = @SecurityRequirement(name = "basic"), summary = "Login de um determinado usuário", description = "Login de um determinado usuário")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Login de usuário") })
    @GetMapping(value = "/login", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserAuthDTO>> login(Authentication authentication) {
        return success(new UserAuthDTO(authenticationService.authenticate(authentication)));
    }

    @GetMapping(value = "/teste-acao", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<String>> teste() {
        return success("Ok, funcionou");
    }

    /**
     * Resets the password of a user using the provided credentials.
     *
     * @param userDTO A {@link UserBasicDTO} containing the user's credentials.
     *
     * @return A {@link ResponseEntity} wrapping an {@code ApiResponseWrapper<Void>} indicating success or failure.
     * @throws RokaMokaContentNotFoundException if the user specified in the request is not found.
     * @throws RokaMokaForbiddenException if the user does not have permission to perform this action.
     */
    @Operation(
            summary = "Redefinição de senha do usuário",
            description = "Permite que um usuário redefina sua senha fornecendo suas credenciais"
    )
    @PostMapping(
            value = "/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseWrapper<Void>> resetPassword(@RequestBody UserResetPasswordDTO userDTO)
            throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        this.IUserService.resetUserPassword(userDTO);
        return success();
    }

    @GetMapping(value = "/me")
    public ResponseEntity<ApiResponseWrapper<UserOutputDTO>> getLoggedUserInformation()
            throws RokaMokaContentNotFoundException {
        return success(this.IUserService.getLoggedUserInformation());
    }
}
