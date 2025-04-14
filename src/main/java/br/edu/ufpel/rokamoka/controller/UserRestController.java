package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicated;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.UserService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.edu.ufpel.rokamoka.dto.user.input.UserBasicDTO;
import br.edu.ufpel.rokamoka.dto.user.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    /**
     * Creates a new user using the provided data.
     *
     * @param userDTO A {@link UserBasicDTO} containing the user's name, email and password.
     * @return A {@link ResponseEntity} containing the created user's JWT
     * @throws RokaMokaContentDuplicatedException if the email or name are already in use.
     */
    @PostMapping("/normal/create")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserBasicDTO userDTO)
            throws RokaMokaContentDuplicatedException {
        return ResponseEntity.ok(userService.createNormalUser(userDTO));
    }

    /**
     * Authenticates a user and generates a JWT.
     *
     * @param authentication An {@link Authentication} object containing the user's credentials.
     * @return A {@link ResponseEntity} containing the user's JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<UserResponseDTO>> login(Authentication authentication) {
        return success(new UserResponseDTO(authenticationService.authenticate(authentication)));
    }
}
