package br.edu.ufpel.rokamoka.service.artwork;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface IArtworkService {

    Artwork getArtworkOrElseThrow(Long id);

    Optional<Artwork> findByQrCode(String qrCode);

    Artwork getByQrCodeOrThrow(String qrCode);

    Artwork create(Long exhibitionId, @Valid ArtworkInputDTO artworkInputDTO);

    List<Artwork> getAllArtworkByExhibitionId(Long exhibitionId);

    List<ArtworkOutputDTO> addArtworksToExhibition(List<ArtworkInputDTO> inputList, Exhibition exhibition);

    List<ArtworkOutputDTO> deleteByExhibitionId(Long exhibitionId);

    ArtworkOutputDTO update(ArtworkInputDTO input);

    ArtworkOutputDTO delete(Long id);
}
