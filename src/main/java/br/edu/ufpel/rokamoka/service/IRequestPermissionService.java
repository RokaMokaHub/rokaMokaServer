package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;

public interface IRequestPermissionService {

    PermissionRequestStatusDTO createRequest(String userName, RoleEnum role) throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException;

    PermissionRequestStatusDTO getPermissionRequestStatus() throws RokaMokaContentNotFoundException;
}
