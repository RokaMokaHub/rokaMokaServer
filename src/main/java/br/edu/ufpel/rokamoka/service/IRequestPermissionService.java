package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;

import java.util.List;

public interface IRequestPermissionService {

    PermissionRequestStatusDTO createRequest(String userName, RoleEnum role);

    List<PermissionRequestStatusDTO> getAllPermissionRequestStatusByLoggedUser();
}
