package br.edu.ufpel.rokamoka.service.artwork;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;

import java.util.List;

public interface IArtworkService {

    Artwork create(Long exhibitionId, ArtworkInputDTO artworkInputDTO);

    Artwork update(ArtworkInputDTO input);

    ArtworkOutputDTO delete(Long id);

    Artwork getArtworkOrElseThrow(Long id);

    Artwork getByQrCodeOrThrow(String qrCode);

    List<Artwork> getAllArtworkByExhibitionId(Long exhibitionId);

    List<ArtworkOutputDTO> addArtworksToExhibition(List<ArtworkInputDTO> inputList, Exhibition exhibition);

    List<ArtworkOutputDTO> deleteByExhibitionId(Long exhibitionId);
}
