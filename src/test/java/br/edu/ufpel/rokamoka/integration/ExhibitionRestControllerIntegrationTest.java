package br.edu.ufpel.rokamoka.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
public class ExhibitionRestControllerIntegrationTest extends BaseIT{
    
    
    @Test
    public void dockerWorks() {
        assertTrue(postgres.isRunning());
    }
}
