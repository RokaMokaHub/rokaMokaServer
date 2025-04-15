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

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController extends RokaMokaController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<UserResponseDTO>> register(@RequestBody UserBasicDTO userDTO)
            throws RokaMokaContentDuplicated {
        return success(userService.createNormalUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<UserResponseDTO>> login(Authentication authentication) {
        return success(new UserResponseDTO(authenticationService.authenticate(authentication)));
    }
}
