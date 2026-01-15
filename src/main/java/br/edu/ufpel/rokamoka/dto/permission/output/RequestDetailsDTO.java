package br.edu.ufpel.rokamoka.dto.permission.output;

import br.edu.ufpel.rokamoka.core.Role;

import java.time.LocalDateTime;

public record RequestDetailsDTO(Long requestId, String userName, String email, LocalDateTime createdAt, String targetRole) {

    public RequestDetailsDTO(Long requestId, String username, String email, LocalDateTime createdAt, Role targetRole) {
        this(requestId, username, email, createdAt, targetRole.getName().getDescription());
    }
}
