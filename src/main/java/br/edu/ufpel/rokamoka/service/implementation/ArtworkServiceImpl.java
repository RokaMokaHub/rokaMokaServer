package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.service.ArtworkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtworkServiceImpl implements ArtworkService {

    @Autowired
    private ArtworkRepository obraRepository;

    @Override
    public List<Artwork> findAll() {
        return obraRepository.findAll();
    }

    @Override
    public Optional<Artwork> findById(Long id) {
        return obraRepository.findById(id);
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
