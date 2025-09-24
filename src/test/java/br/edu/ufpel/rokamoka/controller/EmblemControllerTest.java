package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.service.emblem.EmblemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link EmblemController} class, which is responsible for handling emblem-related endpoints.
 *
 * @author MauricioMucci
 * @see EmblemService
 */
@ExtendWith(MockitoExtension.class)
class EmblemControllerTest implements ControllerResponseValidator {

    @InjectMocks private EmblemController emblemController;

    @Mock private EmblemService emblemService;

    //region findById
    @Test
    void findById() {
    }
    //endregion

    //region register
    @Test
    void register() {
    }
    //endregion

    //region remove
    @Test
    void remove() {
    }
    //endregion
}
