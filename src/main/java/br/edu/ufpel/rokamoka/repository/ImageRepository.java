package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
