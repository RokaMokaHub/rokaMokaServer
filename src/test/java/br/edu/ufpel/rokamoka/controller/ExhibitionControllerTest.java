package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.service.exhibition.ExhibitionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@code ExhibitionController} class, which is responsible for handling exhibition-related
 * endpoints.
 *
 * @author MauricioMucci
 * @see ExhibitionController
 * @see ExhibitionService
 */
@ExtendWith(MockitoExtension.class)
class ExhibitionControllerTest implements ControllerResponseValidator {

    @InjectMocks private ExhibitionController exhibitionController;

    @Mock private ExhibitionService exhibitionService;

    private Exhibition exhibition;
    private ExhibitionInputDTO input;

    @BeforeEach
    void setUp() {
        this.exhibition = Instancio.create(Exhibition.class);
        this.input = mock(ExhibitionInputDTO.class);
    }

    //region getExhibition
    @Test
    void getExhibition() {
    }
    //endregion

    //region getAllExhibitions
    @Test
    void getAllExhibitions() {
    }
    //endregion

    //region register
    @Test
    void register() {
    }
    //endregion

    //region patch
    @Test
    void patch() {
    }
    //endregion

    //region addArtworks
    @Test
    void addArtworks() {
    }
    //endregion

    //region remove
    @Test
    void remove() {
    }
    //endregion
}
