package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

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
     *
     * @return An {@link Optional} containing the Mokadex entity if found, or an empty {@link Optional} if no matching
     * Mokadex is found.
     */
    @Query(value = "SELECT m FROM Mokadex m WHERE m.usuario.nome = :username")
    Optional<Mokadex> findMokadexByUsername(String username);

    /**
     * Checks if all artworks from a specific exhibition have been collected in a specified Mokadex.
     *
     * @param mokadexId The ID of the mokadex to check for artworks.
     * @param exhibitionId The ID of the exhibition containing the artworks to verify.
     *
     * @return {@code true} if all artworks from the specified exhibition are collected in the given Mokadex;
     * {@code false} otherwise.
     */
    @Query("""
           SELECT COUNT(a1) = 0 FROM Artwork a1 WHERE a1.exhibition.id = :exhibitionId AND
           NOT EXISTS (SELECT 1 FROM Mokadex m JOIN m.artworks a2 WHERE a1.id = a2.id and m.id = :mokadexId)""")
    boolean hasCollectedAllArtworksInExhibition(Long mokadexId, Long exhibitionId);

    /**
     * Retrieves all artworks from a specific exhibition that are not yet collected in a specified Mokadex.
     *
     * @param mokadexId The ID of the mokadex to check for missing artworks.
     * @param exhibitionId The ID of the exhibition whose artworks are being checked.
     *
     * @return A {@link Set} containing the {@link Artwork} entities that are part of the specified exhibition but not
     * yet included in the given Mokadex. Returns an empty set if no such artworks are found.
     */
    @Query("""
           SELECT a1 FROM Artwork a1 WHERE a1.exhibition.id = :exhibitionId AND
           NOT EXISTS (SELECT 1 FROM Mokadex m JOIN m.artworks a2 WHERE a1.id = a2.id and m.id = :mokadexId)""")
    Set<Artwork> findAllMissingStars(Long mokadexId, Long exhibitionId);
}
