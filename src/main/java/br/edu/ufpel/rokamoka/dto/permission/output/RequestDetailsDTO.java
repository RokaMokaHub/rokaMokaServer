package br.edu.ufpel.rokamoka.dto.permission.output;

import br.edu.ufpel.rokamoka.core.Role;

public record RequestDetailsDTO(Long requestId, String userName, String email, String targetRole) {

    public RequestDetailsDTO(Long requestId, String username, String email, Role targetRole) {
        this(requestId, username, email, targetRole.getName().getDescription());
    }
}
