package br.edu.ufpel.rokamoka.service.emblem;

import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.repository.EmblemRepository;
import br.edu.ufpel.rokamoka.service.MockRepository;
import br.edu.ufpel.rokamoka.service.exhibition.IExhibitionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    //region findById
    @Test
    void findById() {
    }
    //endregion

    //region findByExhibitionId
    @Test
    void findByExhibitionId() {
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

    //region existsEmblemByExhibitionId
    @Test
    void existsEmblemByExhibitionId() {
    }
    //endregion
}
