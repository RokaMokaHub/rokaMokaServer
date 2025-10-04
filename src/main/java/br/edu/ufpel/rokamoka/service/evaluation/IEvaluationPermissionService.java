package br.edu.ufpel.rokamoka.service.evaluation;

import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;

import java.util.List;

public interface IEvaluationPermissionService {

    void deny(Long permissionId, String justificativa, String userName) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException;

    void accept(Long permissionId, String userName) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException;

    List<RequestDetailsDTO> findAllPendingRequest();
}
