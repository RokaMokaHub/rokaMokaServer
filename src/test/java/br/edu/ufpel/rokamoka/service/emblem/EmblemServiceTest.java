package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.exhibition.IExhibitionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EmblemService} class, which is responsible for handling emblem-related API operations.
 *
 * @author MauricioMucci
 * @see EmblemRepository
 * @see IExhibitionService
 */
@ExtendWith(MockitoExtension.class)
class EmblemServiceTest implements MockRepository<Emblem> {

    @InjectMocks private EmblemService emblemService;

    @Mock private EmblemRepository emblemRepository;
    @Mock private IExhibitionService exhibitionService;

    private Emblem expected;
    private EmblemInputDTO input;
    private Exhibition exhibition;

    @BeforeEach
    void setUp() {
        this.expected = mock(Emblem.class);
        this.input = Instancio.create(EmblemInputDTO.class);
        this.exhibition = mock(Exhibition.class);
    }

    //region findById
    @Test
    void findById_shouldReturnEmblem_whenEmblemExistsById() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(this.expected));

        // Act
        Emblem actual = this.emblemService.findById(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(this.expected, actual);

        verify(this.emblemRepository).findById(anyLong());
    }

    @Test
    void findById_shouldThrowRokaMokaContentNotFoundException_whenEmblemDoesNotExistById() {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.emblemService.findById(1L));

        verify(this.emblemRepository).findById(anyLong());
    }
    //endregion

    //region findByExhibitionId
    @Test
    void findByExhibitionId_shouldReturnEmblem_whenEmblemExistsByExhibitionId() {
        // Arrange
        when(this.emblemRepository.findEmblemByExhibitionId(anyLong())).thenReturn(Optional.of(this.expected));

        // Act
        Optional<Emblem> actual = this.emblemService.findByExhibitionId(1L);

        // Assert
        assertNotNull(actual);
        assertTrue(actual.isPresent());

        verify(this.emblemRepository).findEmblemByExhibitionId(anyLong());
    }

    @Test
    void findByExhibitionId_shouldReturnEmpty_whenEmblemDoesNotExistByExhibitionId() {
        // Arrange
        when(this.emblemRepository.findEmblemByExhibitionId(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Emblem> actual = this.emblemService.findByExhibitionId(1L);

        // Assert
        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(this.emblemRepository).findEmblemByExhibitionId(anyLong());
    }
    //endregion

    //region create
    @Test
    void create_shouldReturnEmblem_whenSuccessful()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.exhibitionService.findById(anyLong())).thenReturn(this.exhibition);
        when(this.emblemRepository.save(any(Emblem.class)))
                .thenAnswer(inv -> this.mockRepositorySave(inv.getArgument(0)));

        // Act
        Emblem actual = this.emblemService.create(this.input);

        // Assert
        assertNotNull(actual);
        assertEquals(this.input.nome(), actual.getNome());
        assertEquals(this.input.descricao(), actual.getDescricao());
        assertEquals(this.exhibition, actual.getExhibition());

        verify(this.exhibitionService).findById(anyLong());
        verify(this.emblemRepository).save(any(Emblem.class));
    }

    @Test
    void create_shouldThrowRokaMokaContentNotFoundException_whenEmblemDoesNotExistById()
    throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.exhibitionService.findById(anyLong())).thenThrow(RokaMokaContentNotFoundException.class);

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.emblemService.create(this.input));

        verify(this.exhibitionService).findById(anyLong());
        verifyNoInteractions(this.emblemRepository);
    }
    //endregion

    //region delete
    @Test
    void delete_shouldReturnEmblem_whenSuccessful() throws RokaMokaContentNotFoundException {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.of(this.expected));

        // Act
        Emblem actual = this.emblemService.delete(1L);

        // Assert
        assertNotNull(actual);
        assertEquals(this.expected, actual);

        verify(this.emblemRepository).findById(anyLong());
    }

    @Test
    void delete_shouldThrowRokaMokaContentNotFoundException_whenEmblemDoesNotExistById() {
        // Arrange
        when(this.emblemRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RokaMokaContentNotFoundException.class, () -> this.emblemService.delete(1L));

        verify(this.emblemRepository).findById(anyLong());
    }
    //endregion

    //region existsEmblemByExhibitionId
    @Test
    void existsEmblemByExhibitionId_shouldReturnTrue_whenExists() {
        // Arrange
        when(this.emblemRepository.existsEmblemByExhibitionId(anyLong())).thenReturn(true);

        // Act
        boolean actual = this.emblemService.existsEmblemByExhibitionId(1L);

        // Assert
        assertTrue(actual);

        verify(this.emblemRepository).existsEmblemByExhibitionId(anyLong());
    }

    @Test
    void existsEmblemByExhibitionId_shouldReturnFale_whenDoesNotExist() {
        // Arrange
        when(this.emblemRepository.existsEmblemByExhibitionId(anyLong())).thenReturn(false);

        // Act
        boolean actual = this.emblemService.existsEmblemByExhibitionId(1L);

        // Assert
        assertFalse(actual);

        verify(this.emblemRepository).existsEmblemByExhibitionId(anyLong());
    }
    //endregion
}
