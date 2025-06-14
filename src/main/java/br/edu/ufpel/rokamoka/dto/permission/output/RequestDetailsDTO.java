package br.edu.ufpel.rokamoka.dto.permission.output;

import br.edu.ufpel.rokamoka.core.Role;
import lombok.Getter;

@Getter
public class RequestDetailsDTO {
    private Long requestId;
    private String userName;
    private String targetRole;

    public RequestDetailsDTO(Long requstId, String username, Role targetRole) {
        this.requestId = requstId;
        this.userName = username;
        this.targetRole = targetRole.getName().getDescription();
    }
}
