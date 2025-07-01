package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.*;
import br.edu.ufpel.rokamoka.dto.permission.output.PermissionRequestStatusDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.PermissionReqRepository;
import br.edu.ufpel.rokamoka.repository.RoleRepository;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import br.edu.ufpel.rokamoka.service.IRequestPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestPermissionService implements IRequestPermissionService {

    private final PermissionReqRepository permissionReqRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public PermissionRequestStatusDTO createRequest(String userName, RoleEnum role) throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        User requester = userRepository.findByNome(userName)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Usuário não encontrado"));

        Role targetRole = roleRepository.findByName(role);

        if (permissionReqRepository.existsByRequesterAndStatusAndTargetRole(
                requester,
                RequestStatus.PENDING,
                targetRole)) {
            throw new RokaMokaContentDuplicatedException("Já existe uma solicitação pendente para este usuário e perfil");
        }

        PermissionReq permissionReq = PermissionReq.builder()
                .requester(requester)
                .status(RequestStatus.PENDING)
                .targetRole(targetRole)
                .build();

        PermissionReq savedRequest = permissionReqRepository.save(permissionReq);

        return new PermissionRequestStatusDTO(
                savedRequest.getId(),
                savedRequest.getStatus(),
                savedRequest.getTargetRole().getName()
        );
    }

    @Override
    public PermissionRequestStatusDTO getPermissionRequestStatus(Long permissionID) throws RokaMokaContentNotFoundException {
        PermissionReq permissionReq = permissionReqRepository.findById(permissionID)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Solicitação não encontrada"));
        return new PermissionRequestStatusDTO(
                permissionReq.getId(),
                permissionReq.getStatus(),
                permissionReq.getTargetRole().getName()
        );
    }
}
