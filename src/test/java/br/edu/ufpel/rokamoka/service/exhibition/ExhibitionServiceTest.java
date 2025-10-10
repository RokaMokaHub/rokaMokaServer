package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.service.location.ILocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@code ExhibitionService} class, which is responsible for handling exhibition-related API
 * operations.
 *
 * @author MauricioMucci
 * @see ExhibitionService
 * @see ExhibitionRepository
 * @see IArtworkService
 * @see ILocationService
 */
@ExtendWith(MockitoExtension.class)
class ExhibitionServiceTest implements MockRepository<Exhibition> {

    @InjectMocks private ExhibitionService exhibitionService;

    @Mock private ExhibitionRepository exhibitionRepository;

    @Mock private IArtworkService artworkService;
    @Mock private ILocationService locationService;

    //region getAllExhibitions
    @Test
    void getAllExhibitions() {
    }
    //endregion

    //region findById
    @Test
    void findById() {
    }
    //endregion

    //region create
    @Test
    void create() {
    }
    //endregion

    //region delete
    @Test
    void delete() {
    }
    //endregion

    //region addArtworks
    @Test
    void addArtworks() {
    }
    //endregion

    //region getExhibitionOrElseThrow
    @Test
    void getExhibitionOrElseThrow() {
    }
    //endregion

    //region update
    @Test
    void update() {
    }
    //endregion
}
