package br.edu.ufpel.rokamoka.service.evaluation;

import br.edu.ufpel.rokamoka.core.PermissionReg;
import br.edu.ufpel.rokamoka.core.PermissionReq;
import br.edu.ufpel.rokamoka.core.RequestStatus;
import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.PermissionRegRepository;
import br.edu.ufpel.rokamoka.repository.PermissionReqRepository;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EvaluationPermissionService implements IEvaluationPermissionService {

    private final PermissionRegRepository registerRepository;
    private final PermissionReqRepository requestRepository;
    private final IUserService userService;

    @Override
    public void deny(Long permissionId, String justificativa, String userName) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        PermissionReq permissionReq = this.requestRepository.findById(permissionId).orElseThrow(RokaMokaContentNotFoundException::new);
        if (permissionReq.getStatus() != RequestStatus.PENDING) {
            throw new RokaMokaForbiddenException("Não é possível aceitar pedido que já foi negado");
        }
        permissionReq.setStatus(RequestStatus.DENY);
        permissionReq = this.requestRepository.save(permissionReq);

        PermissionReg register = PermissionReg.builder()
                .justification(justificativa)
                .reviewer(this.userService.getByNome(userName))
                .request(permissionReq)
                .build();
        this.registerRepository.save(register);
    }

    @Override
    public void accept(Long permissionId, String userName) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        PermissionReq permissionReq = this.requestRepository.findById(permissionId).orElseThrow(RokaMokaContentNotFoundException::new);
        if (permissionReq.getStatus() != RequestStatus.PENDING) {
            throw new RokaMokaForbiddenException("Não é possível aceitar pedido que já foi analisado");
        }
        permissionReq.setStatus(RequestStatus.CONFIRM);
        permissionReq = this.requestRepository.save(permissionReq);
        PermissionReg register = PermissionReg.builder()
                .justification("")
                .reviewer(this.userService.getByNome(userName))
                .request(permissionReq)
                .build();
        this.registerRepository.save(register);

        this.userService.updateRole(permissionReq.getRequester(), permissionReq.getTargetRole());
    }

    @Override
    public List<RequestDetailsDTO> findAllPedingRequest() {
        return this.requestRepository.findAllPendingRequestDetailed();
    }
}
