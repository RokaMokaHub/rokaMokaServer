package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.PermissionReq;
import br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PermissionReqRepository extends JpaRepository<PermissionReq, Long> {

    @Query("""
                SELECT new  br.edu.ufpel.rokamoka.dto.permission.output.RequestDetailsDTO(pr.id, user.nome, pr.targetRole)
                FROM  PermissionReq pr JOIN pr.requester user
                WHERE pr.status = br.edu.ufpel.rokamoka.core.RequestStatus.PENDING
                ORDER  BY  user.nome DESC
            """)
    List<RequestDetailsDTO> findAllPendingRequestDetailed();
}
