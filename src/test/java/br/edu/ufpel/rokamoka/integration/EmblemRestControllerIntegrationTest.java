package br.edu.ufpel.rokamoka.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.edu.ufpel.rokamoka.core.Address;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.repository.LocationRepository;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.repository.UserRepository;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EmblemRestControllerIntegrationTest extends BaseIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MokadexRepository mokadexRepository;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private EmblemRepository emblemRepository;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void consultaDeEmblemaPorId_deveRetornarTodasAsObras_seUsuarioTiverEmblema() throws Exception {
        EmblemSetupData setup = this.setupUsuarioComEmblemaDaExposicao();
        List<Artwork> artworks = this.artworkRepository.findByExhibition_Id(setup.idExposicao())
                .stream()
                .sorted(Comparator.comparing(Artwork::getId))
                .toList();

        this.webTestClient.perform(
                        get("/emblem/{id}", setup.idEmblema())
                                .with(jwt().jwt(jwt -> jwt.subject(setup.nomeUsuario()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.id").exists())
                .andExpect(jsonPath("$.body.exhibition.id").exists())
                .andExpect(jsonPath("$.body.artworks").isArray())
                .andExpect(jsonPath("$.body.artworks.length()").value(10));

        for (int index = 0; index < artworks.size(); index++) {
            Artwork artwork = artworks.get(index);

            this.webTestClient.perform(
                            get("/emblem/{id}", setup.idEmblema())
                                    .with(jwt().jwt(jwt -> jwt.subject(setup.nomeUsuario()))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.body.artworks[" + index + "].id").value(artwork.getId()))
                    .andExpect(jsonPath("$.body.artworks[" + index + "].nome").value(artwork.getNome()))
                    .andExpect(jsonPath("$.body.artworks[" + index + "].nomeArtista").value(artwork.getNomeArtista()))
                    .andExpect(jsonPath("$.body.artworks[" + index + "].descricao").value(artwork.getDescricao()))
                    .andExpect(jsonPath("$.body.artworks[" + index + "].link").value(artwork.getLink()))
                    .andExpect(jsonPath("$.body.artworks[" + index + "].qrCode").value(artwork.getQrCode()));
        }
    }

    @Test
    public void consultaDeEmblemaPorId_naoDeveRetornarTodasAsObras_seUsuarioNaoTiverEmblema() throws Exception {
        EmblemSetupData setup = this.setupUsuarioSemEmblemaDaExposicao();

        this.webTestClient.perform(
                        get("/emblem/{id}", setup.idEmblema())
                                .with(jwt().jwt(jwt -> jwt.subject(setup.nomeUsuario()))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.httpStatus").value(403))
                .andExpect(jsonPath("$.exception").value("RokaMokaForbiddenException"));
    }

    @Test
    public void consultaDeEmblemaPorId_deveRetornar404_quandoEmblemaNaoExistir() throws Exception {
        EmblemSetupData setup = this.setupUsuarioComEmblemaDaExposicao();

        this.webTestClient.perform(
                        get("/emblem/{id}", 999999L)
                                .with(jwt().jwt(jwt -> jwt.subject(setup.nomeUsuario()))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.httpStatus").value(404))
                .andExpect(jsonPath("$.exception").value("RokaMokaContentNotFoundException"));
    }

    private EmblemSetupData setupUsuarioComEmblemaDaExposicao() {
        return this.setupUsuarioComEmblemaDaExposicao(10);
    }

    private EmblemSetupData setupUsuarioSemEmblemaDaExposicao() {
        return this.setupUsuarioComEmblemaDaExposicao(5);
    }

    private EmblemSetupData setupUsuarioComEmblemaDaExposicao(int collectedArtworks) {
        String suffix = UUID.randomUUID().toString();

        Address address = Address.builder()
                .rua("Rua Teste " + suffix)
                .numero("123")
                .cep("96000000")
                .complemento("Apartamento 1")
                .build();

        Location location = Location.builder()
                .nome("Local Teste " + suffix)
                .endereco(address)
                .build();
        Location savedLocation = this.locationRepository.saveAndFlush(location);

        Exhibition exhibition = Exhibition.builder()
                .name("Exposicao Teste " + suffix)
                .description("Exposicao usada em teste de emblema")
                .location(savedLocation)
                .build();
        Exhibition savedExhibition = this.exhibitionRepository.saveAndFlush(exhibition);

        Emblem emblem = new Emblem();
        emblem.setNome("Emblema Teste " + suffix);
        emblem.setDescricao("Emblema vinculado a exposicao de teste");
        emblem.setExhibition(savedExhibition);
        Emblem savedEmblem = this.emblemRepository.saveAndFlush(emblem);

        User user = User.builder()
                .nome("usuario-" + suffix)
                .email("usuario-" + suffix + "@test.local")
                .senha("senha-teste")
                .build();
        User savedUser = this.userRepository.saveAndFlush(user);

        Mokadex mokadex = new Mokadex();
        mokadex.setUsuario(savedUser);
        Mokadex savedMokadex = this.mokadexRepository.saveAndFlush(mokadex);

        var artworks = IntStream.rangeClosed(1, 10)
                .mapToObj(index -> Artwork.builder()
                        .nome("Obra " + index + " - " + suffix)
                        .nomeArtista("Artista " + index + " - " + suffix)
                        .descricao("Obra de teste " + index)
                        .link("https://example.test/artwork/" + suffix + "/" + index)
                        .qrCode("qr-" + suffix + "-" + index)
                        .exhibition(savedExhibition)
                        .build())
                .toList();
        artworks = this.artworkRepository.saveAllAndFlush(artworks);

        savedMokadex.getArtworks().addAll(new HashSet<>(artworks.subList(0, collectedArtworks)));
        if (collectedArtworks == 10) {
            savedMokadex.getEmblems().add(savedEmblem);
        }
        savedMokadex = this.mokadexRepository.saveAndFlush(savedMokadex);

        return new EmblemSetupData(
                savedUser.getId(),
                savedUser.getNome(),
                savedMokadex.getId(),
                savedExhibition.getId(),
                savedEmblem.getId());
    }

    private record EmblemSetupData(
            Long idUsuario,
            String nomeUsuario,
            Long idMokadex,
            Long idExposicao,
            Long idEmblema) {
    }
}
