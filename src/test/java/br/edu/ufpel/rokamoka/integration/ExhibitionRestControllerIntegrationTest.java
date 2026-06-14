package br.edu.ufpel.rokamoka.integration;


import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.repository.ExhibitionRepository;
import lombok.RequiredArgsConstructor;

public class ExhibitionRestControllerIntegrationTest extends BaseIT {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Test
    public void consultaDeEmblemaPorId_deveRetornarTodasAsObras_seUsuarioTiverEmblema() {
        //arrage

        //act
        assertTrue(true);
    }

    @Test
    public void consultaDeEmblemaPorId_naoDeveRetornarTodasAsObras_seUsuarioNaoTiverEmblema() {

    }
}
