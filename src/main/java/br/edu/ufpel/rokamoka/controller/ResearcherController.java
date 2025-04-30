package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.service.UserService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pesquisador", description = "API para cadastros de pesquisadores")
@RestController
@RequestMapping("/researcher")
@RequiredArgsConstructor
public class ResearcherController extends RokaMokaController {

    private final UserService userService;

    /**
     * Creates a new user using the provided data.
     *
     * @param userDTO A {@link UserBasicDTO} containing the user's name, email and password.
     * @return A {@link ResponseEntity} containing the created user's JWT
     * @throws RokaMokaContentDuplicatedException if the email or name are already in use.
     */
    @Operation(summary = "Criação de pesquisador", description = "Cria um pesquisador")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Usuário criado")})
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<UserResponseDTO>> register(@RequestBody UserBasicDTO userDTO)
            throws RokaMokaContentDuplicatedException {
        return success(userService.createReseacher(userDTO));
    }
}
