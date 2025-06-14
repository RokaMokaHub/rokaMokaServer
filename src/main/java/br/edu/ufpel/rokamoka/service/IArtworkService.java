package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

public interface IArtworkService {

    List<Artwork> findAll();

    Artwork findById(Long id) throws RokaMokaContentNotFoundException;

    Artwork save(@Valid Artwork artwork);

    void deleteById(Long id);
}
