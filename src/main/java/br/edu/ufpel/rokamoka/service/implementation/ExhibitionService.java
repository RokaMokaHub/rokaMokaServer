package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.IExhibitionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExhibitionService implements IExhibitionService {

    private ExhibitionRepository exhibitionRepository;
    private ArtworkRepository artworkRepository;

    @Override
    public List<Exhibition> findAll() {
        return this.exhibitionRepository.findAll();
    }

    @Override
    public Exhibition findById(Long id) throws RokaMokaContentNotFoundException {
        return this.exhibitionRepository.findById(id).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    public Exhibition save(ExhibitionInputDTO dto) {
        Exhibition exhibition = Exhibition.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
        List<Artwork> artworks = dto.artworks().stream().map(a -> new Artwork(a, exhibition)).collect(Collectors.toList());
        this.artworkRepository.saveAll(artworks);
        return this.exhibitionRepository.save(exhibition);
    }

    @Override
    public Exhibition save(Long id, ExhibitionInputDTO dto) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.findById(id);
        exhibition.setName(dto.name());
        exhibition.setDescription(dto.description());
        return this.exhibitionRepository.save(exhibition);
    }

    @Override
    public void deleteById(Long id) {
        this.exhibitionRepository.deleteById(id);
    }

}
