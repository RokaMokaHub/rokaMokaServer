package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IArtworkService {

    List<Artwork> findAll();

    Artwork findById(Long id) throws RokaMokaContentNotFoundException;

    Artwork create(Long exhibitionId, @Valid ArtworkInputDTO artworkInputDTO) throws RokaMokaContentNotFoundException;

    void deleteById(Long id);

    void addImage(Long artworkId, MultipartFile image) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException;
}
