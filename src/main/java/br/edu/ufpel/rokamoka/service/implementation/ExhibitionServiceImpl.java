package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.ExhibitionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {

    private ExhibitionRepository exhibitionRepository;
    private ArtworkRepository artworkRepository;

    @Override
    public List<Exhibition> findAll() {
        return exhibitionRepository.findAll();
    }

    @Override
    public Optional<Exhibition> findById(Long id) {
        return exhibitionRepository.findById(id);
    }

    @Override
    public Exhibition save(Exhibition exhibition, List<Artwork> artworks) {
        artworks.stream().forEach(this.artworkRepository::save);
        return exhibitionRepository.save(exhibition);
    }

    @Override
    public void deleteById(Long id) {
        exhibitionRepository.deleteById(id);
    }

}
