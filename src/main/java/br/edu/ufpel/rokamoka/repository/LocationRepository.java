package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByEndereco_Id(Long addressId);
}
