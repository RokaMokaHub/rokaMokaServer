package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
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

    /**
     * Retrieves an Optional containing a Mokadex by a given user.
     *
     * @param usuario The {@link User} object for which the Mokadex entity is being searched.
     * @return An {@code Optional} containing the associated {@link Mokadex}, or an empty {@code Optional}
     * if no match is found for the given user.
     */
    Optional<Mokadex> findMokadexByUsuario(User usuario);

    @Query(value = "SELECT m FROM Mokadex m JOIN m.usuario u WHERE u.nome = :username")
    Optional<Mokadex> findMokadexByUsername(String username);
}
