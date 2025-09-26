package br.edu.ufpel.rokamoka.dto.permission.output;

import br.edu.ufpel.rokamoka.core.PermissionReq;
import br.edu.ufpel.rokamoka.core.RequestStatus;
import br.edu.ufpel.rokamoka.core.RoleEnum;

public record PermissionRequestStatusDTO(Long id, RequestStatus status, RoleEnum targetRole) {

    public PermissionRequestStatusDTO(PermissionReq request) {
        this(request.getId(), request.getStatus(), request.getTargetRole().getName());
    }
}
