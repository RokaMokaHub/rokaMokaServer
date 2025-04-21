package br.edu.ufpel.rokamoka.controller;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.user.input.UserAnonymousDTO;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserAnonymousResponseDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.UserService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


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

    private final UserService userService;
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
    public ResponseEntity<ApiResponseWrapper<UserResponseDTO>> register(@RequestBody UserBasicDTO userDTO)
            throws RokaMokaContentDuplicatedException {
        return success(userService.createNormalUser(userDTO));
    }

    @Operation(summary = "Criação de usuário anônimo", description = "Cria usuário anônimo")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Usuário criado") })
    @PostMapping(value = "/anonymous/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserAnonymousResponseDTO>> anonymousRegister(
            @RequestBody UserAnonymousDTO userDTO)
            throws RokaMokaContentDuplicatedException {
        return success(this.userService.createAnonymousUser(userDTO));
    }

    /**
     * Authenticates a user and generates a JWT.
     *
     * @param authentication An {@link Authentication} object containing the user's credentials.
     * @return A {@link ResponseEntity} containing the user's JWT.
     */
    @Operation(summary = "Login de um determinado usuário", description = "Login de um determinado usuário")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Login de usuário") })
    @GetMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserResponseDTO>> login(Authentication authentication) {
        return success(new UserResponseDTO(authenticationService.authenticate(authentication)));
    }
}
