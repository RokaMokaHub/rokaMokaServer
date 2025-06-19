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
import java.util.stream.Collectors;

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
        return this.exhibitionRepository.findById(id).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    public Exhibition save(ExhibitionInputDTO dto) {
        Exhibition exhibition = Exhibition.builder()
                .name(dto.name())
                .description(dto.description())
                .address(this.addressRepository.save(new Address(dto.enderecoDTO())))
                .build();
        return this.exhibitionRepository.save(exhibition);
    }

    @Override
    public void deleteById(Long id) {
        this.exhibitionRepository.deleteById(id);
    }

    @Override
    public ExhibitionWithArtworksDTO addArtworks(Long exhibitionId, List<ArtworkInputDTO> artworkInputDTOS) throws RokaMokaContentNotFoundException {
        var exhibition = this.exhibitionRepository.findById(exhibitionId).orElseThrow(RokaMokaContentNotFoundException::new);
        List<Artwork> artworks = artworkInputDTOS.stream().map(a -> new Artwork(a, exhibition)).collect(Collectors.toList());
        artworks = this.artworkRepository.saveAll(artworks);
        return new ExhibitionWithArtworksDTO(exhibition.getName(), artworks.stream().map(ArtworkOutputDTO::new).toList());
    }

}
