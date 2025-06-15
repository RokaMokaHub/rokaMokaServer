package br.edu.ufpel.rokamoka.dto.user.input;


import br.edu.ufpel.rokamoka.utils.user.UserNameConstraint;


public record UserAnonymousRequestDTO(
        @UserNameConstraint String userName,
        String deviceId
) {}
