package br.edu.ufpel.rokamoka.service.evaluation;

import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;

import java.util.List;

public interface IEvaluationPermissionService {

    void deny(Long permissionId, String justificativa, String userName);

    void accept(Long permissionId, String userName);

    List<RequestDetailsDTO> findAllPendingRequest();
}
