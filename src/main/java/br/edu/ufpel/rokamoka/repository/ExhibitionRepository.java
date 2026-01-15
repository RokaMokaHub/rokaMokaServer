package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    @Query("""
           SELECT new br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO(e, COUNT(a.id))
           FROM Artwork a
           RIGHT JOIN a.exhibition e
           GROUP BY e
           """)
    List<ExhibitionOutputDTO> findAllExhibitionAndCountArtworks();
}
