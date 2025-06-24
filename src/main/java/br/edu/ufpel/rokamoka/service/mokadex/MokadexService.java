package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.CollectionDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation of the {@link IMokadexService} interface for managing Mokadex-related operations.
 *
 * @author mauri
 * @see MokadexRepository
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class MokadexService implements IMokadexService {

    private final MokadexRepository mokadexRepository;

    private final IUserService userService;
    private final IArtworkService artworkService;

    @Override
    public Optional<Mokadex> getMokadexByUsuario(@Valid User usuario) {
        log.info("Buscando pelo {} do {}", Mokadex.class.getSimpleName(), usuario);

        Optional<Mokadex> maybeMokadex = mokadexRepository.findMokadexByUsuario(usuario);
        if (maybeMokadex.isPresent()) {
            Mokadex mokadex = maybeMokadex.get();
            log.info("{} encontrado: {}", Mokadex.class.getSimpleName(), mokadex.getId());
            return maybeMokadex;
        }

        log.info("Não foi encontrado nenhum {} para o {} informado", Mokadex.class.getSimpleName(), usuario);
        return maybeMokadex;
    }

    @Override
    public MokadexOutputDTO buildMokadexOutputDTOByMokadex(Mokadex mokadex) {
        log.info("Construindo {} para o {} informado", MokadexOutputDTO.class.getSimpleName(), mokadex);

        Set<Artwork> artworkSet = mokadex.getArtworks();
        Set<Exhibition> exhibitionSet = artworkSet.stream()
                .map(Artwork::getExhibition)
                .collect(Collectors.toUnmodifiableSet());

        Set<CollectionDTO> collectionDTOSet = new HashSet<>();
        exhibitionSet.forEach(exhibition -> {

            ExhibitionOutputDTO exhibitionDTO = new ExhibitionOutputDTO(exhibition);
            Set<ArtworkOutputDTO> artworkDTOSet = artworkSet.stream()
                    .filter(artwork -> artwork.getExhibition().equals(exhibition))
                    .map(ArtworkOutputDTO::new)
                    .collect(Collectors.toUnmodifiableSet());
            collectionDTOSet.add(new CollectionDTO(exhibitionDTO, artworkDTOSet));

        });

        MokadexOutputDTO resultDTO = new MokadexOutputDTO(collectionDTOSet);
        log.info("Retornando {} para o {} informado", resultDTO, mokadex);
        return resultDTO;
    }

    @Override
    public MokadexOutputDTO collectStar(@NotBlank String qrCode) throws RokaMokaContentNotFoundException {
        Mokadex mokadex = getLoggedUserMokadexOrThrow();
        Artwork artwork = artworkService.getByQrCodeOrThrow(qrCode);

        if (mokadex.addArtwork(artwork)) {
            log.info("Estrela coletada com sucesso!");
            mokadex = mokadexRepository.save(mokadex);
        } else {
            log.warn("Nenhuma estrela foi coletada");
        }

        return this.buildMokadexOutputDTOByMokadex(mokadex);
    }

    @Override
    public Mokadex getLoggedUserMokadexOrThrow() throws RokaMokaContentNotFoundException {
        return getMokadexByUsuario(userService.findLoggedUser())
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Mokadex não encontrado para usuário logado"));
    }
}
