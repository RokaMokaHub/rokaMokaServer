package br.edu.ufpel.rokamoka.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ufpel.rokamoka.core.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByName(String nome);
}
