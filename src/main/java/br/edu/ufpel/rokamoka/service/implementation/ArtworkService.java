package br.edu.ufpel.rokamoka.service.implementation;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.IArtworkService;
import br.edu.ufpel.rokamoka.service.IIMageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class ArtworkService implements IArtworkService {

    private final ArtworkRepository obraRepository;
    private final IIMageService imageService;
    private final ExhibitionRepository exhibitionRepository;

    @Override
    public List<Artwork> findAll() {
        return obraRepository.findAll();
    }

    @Override
    public Artwork findById(Long id) throws RokaMokaContentNotFoundException {
        return obraRepository.findById(id).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    public Artwork create(Long exhibitionId, @Valid ArtworkInputDTO artworkInputDTO) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.exhibitionRepository.findById(exhibitionId).orElseThrow(RokaMokaContentNotFoundException::new);
        Artwork artwork = Artwork.builder()
                .nome(artworkInputDTO.nome())
                .nomeArtista(artworkInputDTO.nomeArtista())
                .link(artworkInputDTO.link())
                .qrCode(artworkInputDTO.qrCode())
                .images(this.imageService.upload(artworkInputDTO.imageDTO().image()))
                .exhibition(exhibition)
                .build();
        return obraRepository.save(artwork);
    }

    @Override
    public void deleteById(Long id) {
        obraRepository.deleteById(id);
    }

    @Override
    public void addImage(Long artworkId, MultipartFile image) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        var obra = this.obraRepository.findById(artworkId).orElseThrow(RokaMokaContentNotFoundException::new);
        if (!obra.getImages().isEmpty()) {
            throw new RokaMokaForbiddenException("Já existe uma imagem associada a essa obra");
        }
        obra.setImages(this.imageService.upload(image));
        this.obraRepository.save(obra);
    }
}
