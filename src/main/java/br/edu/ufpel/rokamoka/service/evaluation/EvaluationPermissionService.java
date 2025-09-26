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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EvaluationPermissionService implements IEvaluationPermissionService {

    private final PermissionRegRepository registerRepository;
    private final PermissionReqRepository requestRepository;
    private final IUserService userService;

    @Override
    @Transactional(rollbackFor = {RokaMokaContentNotFoundException.class, RokaMokaForbiddenException.class})
    public void deny(Long permissionId, String justificativa, String userName) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        PermissionReq request = this.getPendingPermissionRequestOrElseThrow(permissionId);

        request.setStatus(RequestStatus.DENY);
        request = this.requestRepository.save(request);

        this.createPermissionRegistration(justificativa, userName, request);
    }

    @Override
    @Transactional(rollbackFor = {RokaMokaContentNotFoundException.class, RokaMokaForbiddenException.class})
    public void accept(Long permissionId, String userName) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        PermissionReq request = this.getPendingPermissionRequestOrElseThrow(permissionId);

        request.setStatus(RequestStatus.CONFIRM);
        request = this.requestRepository.save(request);

        this.createPermissionRegistration("", userName, request);

        this.userService.updateRole(request.getRequester(), request.getTargetRole());
    }

    @Override
    public List<RequestDetailsDTO> findAllPendingRequest() {
        return this.requestRepository.findAllPendingRequestDetailed();
    }

    private PermissionReq getPendingPermissionRequestOrElseThrow(Long permissionId)
    throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        PermissionReq permissionReq = this.getRequestOrElseThrow(permissionId);
        if (!permissionReq.isPending()) {
            throw new RokaMokaForbiddenException("Não é possível aceitar/negar pedido que já foi analisado");
        }
        return permissionReq;
    }

    private PermissionReq getRequestOrElseThrow(Long permissionId) throws RokaMokaContentNotFoundException {
        return this.requestRepository.findById(permissionId).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    private PermissionReg createPermissionRegistration(String justificativa, String userName, PermissionReq request)
    throws RokaMokaContentNotFoundException {
        PermissionReg register = PermissionReg.builder()
                .justification(justificativa)
                .reviewer(this.userService.getByNome(userName))
                .request(request)
                .build();
        return this.registerRepository.save(register);
    }
}
