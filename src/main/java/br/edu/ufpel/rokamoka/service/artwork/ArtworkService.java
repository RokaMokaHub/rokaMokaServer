package br.edu.ufpel.rokamoka.service.artwork;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.image.IIMageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ArtworkService implements IArtworkService {

    private final ArtworkRepository artworkRepository;
    private final IIMageService imageService;
    private final ExhibitionRepository exhibitionRepository;

    @Override
    public List<Artwork> findAll() {
        return this.artworkRepository.findAll();
    }

    @Override
    public Artwork findById(Long id) throws RokaMokaContentNotFoundException {
        return this.artworkRepository.findById(id).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    public Optional<Artwork> findByQrCode(String qrCode) {
        log.info("Buscando por obra usando qrCode: {}", qrCode);

        Optional<Artwork> maybeArtwork = this.artworkRepository.findByQrCode(qrCode);
        maybeArtwork.ifPresentOrElse(
                a -> log.info("Encontrou {}", a),
                () -> log.info("Obra não existe!"));

        return maybeArtwork;
    }

    @Override
    public Artwork getByQrCodeOrThrow(String qrCode) throws RokaMokaContentNotFoundException {
        return this.findByQrCode(qrCode)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Obra não encontrada por qrcode"));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Artwork create(Long exhibitionId, @Valid ArtworkInputDTO artworkInputDTO) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.exhibitionRepository.findById(exhibitionId).orElseThrow(RokaMokaContentNotFoundException::new);
        Artwork artwork = Artwork.builder()
                .nome(artworkInputDTO.nome())
                .nomeArtista(artworkInputDTO.nomeArtista())
                .link(artworkInputDTO.link())
                .qrCode(artworkInputDTO.qrCode())
                .images(this.imageService.upload(artworkInputDTO.image()))
                .exhibition(exhibition)
                .build();
        return this.artworkRepository.save(artwork);
    }

    @Override
    public void deleteById(Long id) {
        this.artworkRepository.deleteById(id);
    }

    @Override
    public void addImage(Long artworkId, MultipartFile image) throws RokaMokaContentNotFoundException, RokaMokaForbiddenException {
        var obra = this.artworkRepository.findByIdWithinImage(artworkId).orElseThrow(RokaMokaContentNotFoundException::new);
        if (!obra.getImages().isEmpty()) {
            throw new RokaMokaForbiddenException("Já existe uma imagem associada a essa obra");
        }
        obra.setImages(this.imageService.upload(image));
        this.artworkRepository.save(obra);
    }
}
