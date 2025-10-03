package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.PermissionReq;
import br.edu.ufpel.rokamoka.core.RequestStatus;
import br.edu.ufpel.rokamoka.core.Role;
import br.edu.ufpel.rokamoka.core.RoleEnum;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.PermissionReqRepository;
import br.edu.ufpel.rokamoka.repository.RoleRepository;
import br.edu.ufpel.rokamoka.service.IRequestPermissionService;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestPermissionService implements IRequestPermissionService {

    private final PermissionReqRepository permissionReqRepository;
    private final RoleRepository roleRepository;

    private final IUserService userService;

    @Override
    public PermissionRequestStatusDTO createRequest(String userName, RoleEnum role)
    throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        User requester = this.userService.getByNome(userName);

        Role targetRole = this.roleRepository.findByName(role);

        if (this.permissionReqRepository.existsByRequesterAndStatusAndTargetRole(
                requester, RequestStatus.PENDING, targetRole)) {
            throw new RokaMokaContentDuplicatedException("Já existe uma solicitação pendente para este usuário e perfil");
        }

        PermissionReq permissionReq = PermissionReq.builder()
                .requester(requester)
                .status(RequestStatus.PENDING)
                .targetRole(targetRole)
                .build();
        PermissionReq savedRequest = this.permissionReqRepository.save(permissionReq);

        return new PermissionRequestStatusDTO(savedRequest);
    }

    @Override
    public PermissionRequestStatusDTO getPermissionRequestStatus(Long permissionID)
    throws RokaMokaContentNotFoundException {
        PermissionReq permissionReq = this.permissionReqRepository.findById(permissionID)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Solicitação não encontrada"));
        return new PermissionRequestStatusDTO(permissionReq);
    }
}
