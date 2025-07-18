package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Emblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing the {@link Emblem} resource.
 *
 * @author MauricioMucci
 * @see JpaRepository
 */
public interface EmblemRepository extends JpaRepository<Emblem, Long> {

    /**
     * Checks whether an {@link Emblem} entity exists for a given {@code exhibitionId}.
     *
     * @param exhibitionId The ID of the exhibition to check.
     * @return {@code true} if an {@link Emblem} exists for the given exhibition ID, {@code false} otherwise.
     */
    boolean existsEmblemByExhibitionId(Long exhibitionId);

    /**
     * Retrieves an {@link Emblem} associated with the specified exhibition ID.
     *
     * @param exhibitionId The unique identifier of the exhibition whose emblem needs to be retrieved.
     *
     * @return An {@link Optional} containing the {@link Emblem} if found, or an empty {@link Optional} otherwise.
     */
    Optional<Emblem> findEmblemByExhibitionId(Long exhibitionId);
}
