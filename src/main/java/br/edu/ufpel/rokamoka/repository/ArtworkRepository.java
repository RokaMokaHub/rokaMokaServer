package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
}
