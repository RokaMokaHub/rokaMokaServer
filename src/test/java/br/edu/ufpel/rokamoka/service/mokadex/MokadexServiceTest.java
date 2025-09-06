package br.edu.ufpel.rokamoka.service.mokadex;

import br.edu.ufpel.rokamoka.component.CollectEmblemProducer;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.MokadexRepository;
import br.edu.ufpel.rokamoka.service.artwork.ArtworkService;
import br.edu.ufpel.rokamoka.service.emblem.EmblemService;
import br.edu.ufpel.rokamoka.service.exhibition.ExhibitionService;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link MokadexService} class, which is responsible for handling mokadex-related API operations.
 *
 * @author MauricioMucci
 * @see MokadexRepository
 * @see EmblemService
 * @see ArtworkService
 * @see ExhibitionService
 * @see CollectEmblemProducer
 */
@ExtendWith(MockitoExtension.class)
class MokadexServiceTest {

    @InjectMocks private MokadexService mokadexService;

    @Mock private MokadexRepository mokadexRepository;

    @Mock private EmblemService emblemService;
    @Mock private ArtworkService artworkService;
    @Mock private ExhibitionService exhibitionService;
    @Mock private CollectEmblemProducer collectEmblemProducer;

    //region findById
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void findById_shouldReturnMokadex_whenMokadexExistsById(Long id) throws RokaMokaContentNotFoundException {
        // Arrange
        Mokadex expected = Instancio.of(Mokadex.class)
                .set(Select.field(Mokadex::getId), id)
                .create();

        when(this.mokadexRepository.findById(id)).thenReturn(Optional.of(expected));

        // Act
        Mokadex actual = this.mokadexService.findById(id);

        // Assert
        assertEquals(expected, actual);

        verify(this.mokadexRepository).findById(id);
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenMokadexDoesNotExistById() {
        // Arrange
        when(this.mokadexRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.mokadexService.findById(1L));

        verify(this.mokadexRepository).findById(anyLong());
    }
    //endregion

    //region getOrCreateMokadexByUser
    @Test
    void getOrCreateMokadexByUser_shouldGetMokadex_whenMokadexExistsByUser() {
        // Arrange
        User user = Instancio.create(User.class);
        Mokadex expected = Instancio.of(Mokadex.class)
                .set(Select.field(Mokadex::getUsuario), user)
                .create();

        when(this.mokadexRepository.findMokadexByUsername(user.getNome())).thenReturn(Optional.of(expected));

        // Act
        Mokadex actual = this.mokadexService.getOrCreateMokadexByUser(user);

        // Assert
        assertEquals(expected, actual);

        verify(this.mokadexRepository, times(1)).findMokadexByUsername(user.getNome());
        verifyNoMoreInteractions(this.mokadexRepository);
        verifyNoInteractions(this.emblemService, this.artworkService, this.exhibitionService, this.collectEmblemProducer);
    }

    @Test
    void getOrCreateMokadexByUser_shouldCreateMokadex_whenMokadexDoesNotExistByUser() {
        // Arrange
        User user = Instancio.create(User.class);

        when(this.mokadexRepository.findMokadexByUsername(user.getNome())).thenReturn(Optional.empty());
        when(this.mokadexRepository.save(any(Mokadex.class))).thenAnswer(
                inv -> this.mockMokadexRepositorySave(inv.getArgument(0)));

        // Act
        Mokadex actual = this.mokadexService.getOrCreateMokadexByUser(user);

        // Assert
        assertNotNull(actual);
        assertEquals(user, actual.getUsuario());

        verify(this.mokadexRepository, times(1)).save(any(Mokadex.class));
        verifyNoMoreInteractions(this.mokadexRepository);
        verifyNoInteractions(this.emblemService, this.artworkService, this.exhibitionService, this.collectEmblemProducer);
    }

    private Mokadex mockMokadexRepositorySave(Mokadex mokadex) {
        mokadex.setId(1L);
        return mokadex;
    }
    //endregion

    //region getMokadexOutputDTOByMokadex
    @Test
    void getMokadexOutputDTOByMokadex_shouldBuildMokadexOutputDTO_whenCalled() {
        // Arrange
        Mokadex mokadex = Instancio.create(Mokadex.class);

        // Act
        MokadexOutputDTO actual = this.mokadexService.getMokadexOutputDTOByMokadex(mokadex);

        // Assert
        assertNotNull(actual);
        assertNotNull(actual.collectionSet());
        assertNotNull(actual.emblemSet());
    }
    //endregion

    // TODO bellow
    //region collectStar
    @Test
    void collectStar() {
    }
    //endregion

    //region collectEmblem
    @Test
    void collectEmblem() {
    }
    //endregion

    //region getMissingStarsByExhibition
    @Test
    void getMissingStarsByExhibition() {
    }
    //endregion
}
