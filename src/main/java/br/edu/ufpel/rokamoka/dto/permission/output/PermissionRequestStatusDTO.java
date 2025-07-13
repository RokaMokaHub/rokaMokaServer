package br.edu.ufpel.rokamoka.dto.permission.output;

import br.edu.ufpel.rokamoka.core.RequestStatus;
import br.edu.ufpel.rokamoka.core.RoleEnum;

public record PermissionRequestStatusDTO(Long id, RequestStatus status, RoleEnum targetRole) {
}
