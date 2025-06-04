package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.service.ArtworkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private ArtworkRepository obraRepository;

    @Override
    public List<Artwork> findAll() {
        return obraRepository.findAll();
    }

    @Override
    public Artwork findById(Long id) throws RokaMokaContentNotFoundException {
        return obraRepository.findById(id).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    public Artwork save(@Valid Artwork artwork) {
        return obraRepository.save(artwork);
    }

    @Override
    public void deleteById(Long id) {
        obraRepository.deleteById(id);
    }
}
