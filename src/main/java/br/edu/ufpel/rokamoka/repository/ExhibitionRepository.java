package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
}
