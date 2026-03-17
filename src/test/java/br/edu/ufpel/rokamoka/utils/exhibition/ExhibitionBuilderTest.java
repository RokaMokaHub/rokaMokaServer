package br.edu.ufpel.rokamoka.utils.exhibition;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * Tests the {@link ExhibitionBuilder} creation and update flows, ensuring that exhibitions are built or patched
 * correctly and that invalid builder states result in the expected exceptions.
 *
 * @author MauricioMucci
 * @see ExhibitionBuilder
 */
class ExhibitionBuilderTest {

    //region build
    @Test
    void build_shouldThrowIllegalStateException_whenExhibitionIsNotNull() {
        // Arrange
        Exhibition exhibition = mock(Exhibition.class);
        Location location = mock(Location.class);
        ExhibitionInputDTO input = mock(ExhibitionInputDTO.class);

        ExhibitionBuilder builder = new ExhibitionBuilder(exhibition, location, input);

        // Act & Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void build_shouldBuildExhibition_whenExhibitionIsNull() {
        // Arrange
        Location location = Instancio.create(Location.class);
        ExhibitionInputDTO input = Instancio.create(ExhibitionInputDTO.class);

        ExhibitionBuilder builder = new ExhibitionBuilder(location, input);

        // Act
        Exhibition result = builder.build();

        // Assert
        assertNotNull(result);
        assertEquals(location, result.getLocation());
        assertEquals(input.name(), result.getName());
        assertEquals(input.description(), result.getDescription());
    }
    //endregion

    //region update
    @Test
    void update_shouldThrowIllegalStateException_whenExhibitionIsNull() {
        // Arrange
        Location location = mock(Location.class);
        ExhibitionInputDTO input = mock(ExhibitionInputDTO.class);

        ExhibitionBuilder builder = new ExhibitionBuilder(location, input);

        // Act & Assert
        assertThrows(IllegalStateException.class, builder::update);
    }

    @Test
    void update_shouldPatchExhibition_whenExhibitionIsNotNull() {
        // Arrange
        Exhibition exhibition = Instancio.create(Exhibition.class);
        Location location = Instancio.create(Location.class);
        ExhibitionInputDTO input = Instancio.create(ExhibitionInputDTO.class);

        ExhibitionBuilder builder = new ExhibitionBuilder(exhibition, location, input);

        // Act
        Exhibition result = builder.update();

        // Assert
        assertNotNull(result);
        assertEquals(location, result.getLocation());
        assertEquals(input.name(), result.getName());
        assertEquals(input.description(), result.getDescription());
    }
    //endregion
}
