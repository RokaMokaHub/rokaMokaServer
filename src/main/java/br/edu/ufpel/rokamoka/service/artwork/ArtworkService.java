package br.edu.ufpel.rokamoka.service.artwork;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.image.IIMageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@AllArgsConstructor
public class ArtworkService implements IArtworkService {

    private final ArtworkRepository artworkRepository;
    private final IIMageService imageService;
    private final ExhibitionRepository exhibitionRepository;

    @Override
    @Transactional(readOnly = true)
    public Artwork getArtworkOrElseThrow(Long id) {
        return this.artworkRepository.findById(id).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Artwork> findByQrCode(String qrCode) {
        return this.artworkRepository.findByQrCode(qrCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Artwork getByQrCodeOrThrow(String qrCode) {
        return this.findByQrCode(qrCode).orElseThrow(RokaMokaContentNotFoundException::new);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public Artwork create(Long exhibitionId, @Valid ArtworkInputDTO artworkInputDTO) {
        var exhibition = this.exhibitionRepository.findById(exhibitionId)
                .orElseThrow(RokaMokaContentNotFoundException::new);
        var images = this.imageService.upload(artworkInputDTO.image());

        var artwork = Artwork.builder()
                .nome(artworkInputDTO.nome())
                .descricao(artworkInputDTO.descricao())
                .nomeArtista(artworkInputDTO.nomeArtista())
                .link(artworkInputDTO.link())
                .qrCode(artworkInputDTO.qrCode())
                .images(images)
                .exhibition(exhibition)
                .build();
        return this.artworkRepository.save(artwork);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Artwork> getAllArtworkByExhibitionId(Long exhibitionId) {
        return this.artworkRepository.findByExhibition_Id(exhibitionId);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public List<ArtworkOutputDTO> addArtworksToExhibition(List<ArtworkInputDTO> inputList, Exhibition exhibition) {
        var artworks = inputList.stream().map(a -> new Artwork(a, exhibition)).toList();
        artworks = this.artworkRepository.saveAll(artworks);
        return artworks.stream().map(ArtworkOutputDTO::new).toList();
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public List<ArtworkOutputDTO> deleteByExhibitionId(Long exhibitionId) {
        var artworks = this.getAllArtworkByExhibitionId(exhibitionId);
        this.artworkRepository.deleteAllById(artworks.stream().map(Artwork::getId).toList());
        return artworks.stream().map(ArtworkOutputDTO::new).toList();
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public ArtworkOutputDTO update(ArtworkInputDTO input) {
        var artwork = this.getArtworkOrElseThrow(input.id());

        artwork.setNome(input.nome());
        artwork.setDescricao(input.descricao());
        artwork.setNomeArtista(input.nomeArtista());
        artwork.setLink(input.link());
        artwork.setQrCode(input.qrCode());

        var images = this.imageService.upload(input.image());
        if (!CollectionUtils.isEmpty(images)) {
            artwork.getImages().clear();
            artwork.getImages().addAll(images);
        }

        return new ArtworkOutputDTO(artwork);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public ArtworkOutputDTO delete(Long id) {
        var artwork = this.getArtworkOrElseThrow(id);
        this.artworkRepository.delete(artwork);
        return new ArtworkOutputDTO(artwork);
    }
}
