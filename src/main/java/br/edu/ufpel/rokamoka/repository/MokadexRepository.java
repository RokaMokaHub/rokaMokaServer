package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author mauri
 */
public interface MokadexRepository extends JpaRepository<Mokadex, Long> {

    Optional<Mokadex> findMokadexByUsuario(User usuario);
}
