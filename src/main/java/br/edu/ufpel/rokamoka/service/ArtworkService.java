package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.core.Artwork;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface ArtworkService {

    List<Artwork> findAll();

    Optional<Artwork> findById(Long id);

    Artwork save(@Valid Artwork artwork);

    void deleteById(Long id);
}
