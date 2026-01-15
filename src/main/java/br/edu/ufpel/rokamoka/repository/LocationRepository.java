package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByEndereco_Id(Long addressId);

    boolean existsByNome(String nome);
}
