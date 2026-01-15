package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByRuaAndNumeroAndCep(String rua, String numero, String cep);

    Optional<Address> findByRuaAndNumeroAndCepAndComplemento(String rua, String numero, String cep, String complemento);
}
