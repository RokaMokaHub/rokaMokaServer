package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository interface for managing the {@link Mokadex} resource.
 *
 * @author MauricioMucci
 * @see JpaRepository
 */
public interface MokadexRepository extends JpaRepository<Mokadex, Long> {

    /**
     * Retrieves a Mokadex entity associated with a specific username.
     *
     * @param username The username of the {@link User} associated with the desired Mokadex.
     * @return An {@link Optional} containing the Mokadex entity if found,
     *         or an empty {@link Optional} if no matching Mokadex is found.
     */
    @Query(value = "SELECT m FROM Mokadex m WHERE m.usuario.nome = :username")
    Optional<Mokadex> findMokadexByUsername(String username);
}
