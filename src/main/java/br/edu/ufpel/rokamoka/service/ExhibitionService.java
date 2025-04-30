package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;

import java.util.List;
import java.util.Optional;

public interface ExhibitionService {

    List<Exhibition> findAll();

    Optional<Exhibition> findById(Long id);

    Exhibition save(Exhibition exhibition, List<Artwork> artworks);

    void deleteById(Long id);
}
