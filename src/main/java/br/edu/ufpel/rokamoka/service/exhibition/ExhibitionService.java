package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.service.location.ILocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ExhibitionService implements IExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final IArtworkService artworkService;
    private final ILocationService locationService;

    @Override
    public List<ExhibitionOutputDTO> getAllExhibitions() {
        return this.exhibitionRepository.findAllExhibitionAndCountArtworks();
    }

    @Override
    public ExhibitionOutputDTO findById(Long id) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.getExhibitionOrElseThrow(id);

        this.loadArtworks(exhibition);

        return new ExhibitionOutputDTO(exhibition);
    }

    private void loadArtworks(Exhibition exhibition) {
        List<Artwork> artworks = this.artworkService.getAllArtworkByExhibitionId(exhibition.getId());
        exhibition.setArtworks(artworks);
    }

    @Override
    @Transactional
    public ExhibitionOutputDTO create(ExhibitionInputDTO dto) throws RokaMokaContentNotFoundException {
        Location location = this.locationService.getLocationOrElseThrow(dto.locationId());
        Exhibition exhibition = Exhibition.builder()
                .name(dto.name())
                .description(dto.description())
                .location(location)
                .build();
        exhibition = this.exhibitionRepository.save(exhibition);
        return new ExhibitionOutputDTO(exhibition);
    }

    @Override
    public ExhibitionOutputDTO delete(Long id) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.getExhibitionOrElseThrow(id);

        List<ArtworkOutputDTO> artworks = this.artworkService.deleteByExhibitionId(exhibition.getId());

        this.exhibitionRepository.delete(exhibition);
        return new ExhibitionOutputDTO(exhibition);
    }

    @Override
    @Transactional
    public ExhibitionOutputDTO addArtworks(Long id, List<ArtworkInputDTO> inputList)
    throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.getExhibitionOrElseThrow(id);
        List<ArtworkOutputDTO> artworks = this.artworkService.addArtworksToExhibition(inputList, exhibition);
        return new ExhibitionOutputDTO(exhibition, (long) artworks.size());
    }

    @Override
    public Exhibition getExhibitionOrElseThrow(Long id) throws RokaMokaContentNotFoundException {
        return this.exhibitionRepository.findById(id).orElseThrow(() ->
                new RokaMokaContentNotFoundException("Exposição não encontrada"));
    }
}
