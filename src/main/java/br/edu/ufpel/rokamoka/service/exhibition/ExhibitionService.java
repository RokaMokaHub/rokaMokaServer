package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Address;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionWithArtworksDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.AddressRepository;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExhibitionService implements IExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ArtworkRepository artworkRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<Exhibition> findAll() {
        return this.exhibitionRepository.findAll();
    }

    @Override
    public Exhibition findById(Long id) throws RokaMokaContentNotFoundException {
        return this.getOrElseThrow(id);
    }

    private Exhibition getOrElseThrow(Long exhibitionId) throws RokaMokaContentNotFoundException {
        return this.exhibitionRepository.findById(exhibitionId).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    public Exhibition save(ExhibitionInputDTO dto) {
        Exhibition exhibition = Exhibition.builder()
                .name(dto.name())
                .description(dto.description())
                .address(this.addressRepository.save(new Address(dto.addressInputDTO())))
                .build();
        return this.exhibitionRepository.save(exhibition);
    }

    @Override
    public void deleteById(Long id) {
        this.exhibitionRepository.deleteById(id);
    }

    @Override
    public ExhibitionWithArtworksDTO addArtworks(Long exhibitionId, List<ArtworkInputDTO> artworkInputDTOS)
    throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.getOrElseThrow(exhibitionId);
        List<ArtworkOutputDTO> artworks = this.createArtworksForExhibition(exhibition, artworkInputDTOS);
        return new ExhibitionWithArtworksDTO(exhibition.getName(), artworks);
    }

    private List<ArtworkOutputDTO> createArtworksForExhibition(Exhibition exhibition, List<ArtworkInputDTO> artworkInputDTOS) {
        List<Artwork> artworks = artworkInputDTOS.stream()
                .map(a -> new Artwork(a, exhibition))
                .toList();
        return this.artworkRepository.saveAll(artworks)
                .stream()
                .map(ArtworkOutputDTO::new)
                .toList();
    }
}
