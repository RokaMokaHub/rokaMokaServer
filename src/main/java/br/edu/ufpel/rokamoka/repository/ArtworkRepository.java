package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    @Query("""
           SELECT NEW br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO(a, i)
           FROM Artwork a LEFT JOIN FETCH a.images i
           WHERE a.id = ?1
           """)
    ArtworkOutputDTO createFullArtworkInfo(Long id);

    Optional<Artwork> findByQrCode(String qrCode);

    List<Artwork> findByExhibition_Id(Long exhibitionId);
}
