package br.edu.ufpel.rokamoka.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufpel.rokamoka.dto.input.NewUserDTO;
import br.edu.ufpel.rokamoka.dto.output.UserResponseDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicated;
import br.edu.ufpel.rokamoka.security.AuthenticationService;
import br.edu.ufpel.rokamoka.service.UserService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Validated NewUserDTO userDTO)
            throws RokaMokaContentDuplicated {
        UserResponseDTO responseDTO = userService.createNormalUser(userDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(Authentication authentication) throws RokaMokaContentDuplicated {
        return ResponseEntity.ok(new UserResponseDTO(authenticationService.authenticate(authentication)));
    }
}
