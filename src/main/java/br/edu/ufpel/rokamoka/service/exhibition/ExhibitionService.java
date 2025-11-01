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
import br.edu.ufpel.rokamoka.utils.exhibition.ExhibitionBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

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
    public ExhibitionOutputDTO getExhibitionWithArtworks(Long id) {
        Exhibition exhibition = this.getExhibitionOrElseThrow(id);
        this.loadArtworks(exhibition);
        return toOutput(exhibition);
    }

    private void loadArtworks(Exhibition exhibition) {
        List<Artwork> artworks = this.artworkService.getAllArtworkByExhibitionId(exhibition.getId());
        exhibition.setArtworks(artworks);
    }

    private static ExhibitionOutputDTO toOutput(Exhibition exhibition) {
        return new ExhibitionOutputDTO(exhibition);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public ExhibitionOutputDTO create(ExhibitionInputDTO dto) {
        Location location = this.locationService.getLocationOrElseThrow(dto.locationId());
        Exhibition exhibition = new ExhibitionBuilder(dto, location).build();
        exhibition = this.exhibitionRepository.save(exhibition);
        return toOutput(exhibition);
    }

    @Override
    public ExhibitionOutputDTO delete(Long id) {
        Exhibition exhibition = this.getExhibitionOrElseThrow(id);

        this.artworkService.deleteByExhibitionId(exhibition.getId());

        this.exhibitionRepository.delete(exhibition);
        return toOutput(exhibition);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public ExhibitionOutputDTO addArtworks(Long id, List<ArtworkInputDTO> inputList) {
        Exhibition exhibition = this.getExhibitionOrElseThrow(id);
        List<ArtworkOutputDTO> artworks = this.artworkService.addArtworksToExhibition(inputList, exhibition);
        return new ExhibitionOutputDTO(exhibition, (long) artworks.size());
    }

    @Override
    public Exhibition getExhibitionOrElseThrow(Long id) {
        return this.exhibitionRepository.findById(id)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Exposição não encontrada"));
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public ExhibitionOutputDTO update(ExhibitionInputDTO input) {
        Exhibition exhibition = this.getExhibitionOrElseThrow(input.id());

        Location location = exhibition.getLocation();
        if (input.locationId() != null) {
            location = this.locationService.getLocationOrElseThrow(input.locationId());
        }

        exhibition = new ExhibitionBuilder(exhibition.getId(), input, location).build();
        exhibition = this.exhibitionRepository.save(exhibition);

        return toOutput(exhibition);
    }
}
