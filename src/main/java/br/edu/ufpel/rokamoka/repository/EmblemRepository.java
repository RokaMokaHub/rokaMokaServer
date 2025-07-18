package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Emblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

/**
 * @author MauricioMucci
 */
public interface EmblemRepository extends JpaRepository<Emblem, Long> {

    boolean existsEmblemByExhibitionId(Long exhibitionId);

    @Query("""
           SELECT COUNT(a1) = 0 FROM Artwork a1 WHERE a1.exhibition.id = :exhibitionId AND
           NOT EXISTS (SELECT 1 FROM Mokadex m JOIN m.artworks a2 WHERE a1.id = a2.id and m.id = :mokadexId)""")
    boolean hasCollectedAllArtworksInExhibition(Long mokadexId, Long exhibitionId);

    Optional<Emblem> findEmblemByExhibitionId(Long exhibitionId);

    @Query("SELECT e FROM Mokadex m JOIN m.emblems e WHERE m.id = :mokadexId")
    Set<Emblem> findEmblemsByMokadexId(Long mokadexId);
}
