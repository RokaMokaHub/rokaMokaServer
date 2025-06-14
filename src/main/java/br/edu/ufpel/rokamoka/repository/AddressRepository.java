package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
