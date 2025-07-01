package br.edu.ufpel.rokamoka.dto.permission.output;

import br.edu.ufpel.rokamoka.core.RequestStatus;
import br.edu.ufpel.rokamoka.core.RoleEnum;
import lombok.Data;

@Data
public class PermissionRequestStatusDTO {
    private long id;
    private RequestStatus status;
    private RoleEnum targetRole;

    public PermissionRequestStatusDTO(Long id, RequestStatus status, RoleEnum targetRole) {
        this.id = id;
        this.status = status;
        this.targetRole = targetRole;
    }
}
