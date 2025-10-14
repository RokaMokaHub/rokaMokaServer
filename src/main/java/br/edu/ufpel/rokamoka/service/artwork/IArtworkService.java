package br.edu.ufpel.rokamoka.service.artwork;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IArtworkService {

    List<Artwork> findAll();

    Artwork getArtworkOrElseThrow(Long id) throws RokaMokaContentNotFoundException;

    Optional<Artwork> findByQrCode(String qrCode);

    Artwork getByQrCodeOrThrow(String qrCode) throws RokaMokaContentNotFoundException;

    Artwork create(Long exhibitionId, @Valid ArtworkInputDTO artworkInputDTO) throws RokaMokaContentNotFoundException;

    void deleteById(Long id);

    void addImage(Long artworkId, MultipartFile image) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException;

    List<Artwork> getAllArtworkByExhibitionId(Long exhibitionId);

    List<ArtworkOutputDTO> addArtworksToExhibition(List<ArtworkInputDTO> inputList, Exhibition exhibition);

    List<ArtworkOutputDTO> deleteByExhibitionId(Long exhibitionId);
}
