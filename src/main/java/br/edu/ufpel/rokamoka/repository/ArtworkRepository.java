package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    @Query("""
            select new br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO(a, i)
                from Artwork a left join fetch a.images i
                where a.id = ?1
            """)
    ArtworkOutputDTO createFullArtworkInfo(Long id);

    @Query("""
            select a
                from Artwork a left join fetch a.images i
                where a.id = ?1
            """)
    Optional<Artwork> findByIdWithinImage(Long id);
}
