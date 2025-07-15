package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Emblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author MauricioMucci
 */
public interface EmblemRepository extends JpaRepository<Emblem, Long> {

    @Query(value = """
                   SELECT e FROM Emblem e
                   JOIN e.exhibition exh
                   JOIN Artwork a on a.exhibition = exh
                   WHERE a.id = :artworkId""")
    Optional<Emblem> findEmblemByArtworkId(Long artworkId);

    @Query("""
       SELECT count(a1) = 0
       FROM Artwork a1
       WHERE a1.exhibition.id = (SELECT a2.exhibition.id FROM Artwork a2 WHERE a2.id = :artworkId)
       AND NOT EXISTS (SELECT 1 FROM Mokadex m JOIN m.artworks a3 WHERE m.id = a3.id AND m.id = :mokadexId)
       """)
    boolean hasCollectedAllArtworksInExhibition(@Param("mokadexId") Long mokadexId, @Param("artworkId") Long artworkId);
}
