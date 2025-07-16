package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Mokadex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository interface for managing Mokadex entities.
 *
 * @author mauri
 * @see Mokadex
 * @see JpaRepository
 */
public interface MokadexRepository extends JpaRepository<Mokadex, Long> {

    @Query(value = "SELECT m FROM Mokadex m JOIN m.usuario u WHERE u.nome = :username")
    Optional<Mokadex> findMokadexByUsername(String username);
}
