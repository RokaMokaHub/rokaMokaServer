package br.edu.ufpel.rokamoka.utils.exhibition;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
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
        var exhibition = mock(Exhibition.class);
        var location = mock(Location.class);
        var input = mock(ExhibitionInputDTO.class);

        var builder = new ExhibitionBuilder(exhibition, location, input);

        // Act & Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void build_shouldBuildExhibition_whenExhibitionIsNull() {
        // Arrange
        var location = Instancio.create(Location.class);
        var input = Instancio.create(ExhibitionInputDTO.class);

        var builder = new ExhibitionBuilder(location, input);

        // Act
        var result = builder.build();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(location, result.getLocation()),
                () -> assertEquals(input.name(), result.getName()),
                () -> assertEquals(input.description(), result.getDescription())
        );
    }
    //endregion

    //region update
    @Test
    void update_shouldThrowIllegalStateException_whenExhibitionIsNull() {
        // Arrange
        var location = mock(Location.class);
        var input = mock(ExhibitionInputDTO.class);

        var builder = new ExhibitionBuilder(location, input);

        // Act & Assert
        assertThrows(IllegalStateException.class, builder::update);
    }

    @Test
    void update_shouldPatchExhibition_whenExhibitionIsNotNull() {
        // Arrange
        var location = Instancio.create(Location.class);
        var input = Instancio.create(ExhibitionInputDTO.class);

        var exhibition = Instancio.create(Exhibition.class);
        var createdBy = exhibition.getCreatedBy();
        var createdDate = exhibition.getCreatedDate();

        var builder = new ExhibitionBuilder(exhibition, location, input);

        // Act
        var result = builder.update();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(location, result.getLocation()),
                () -> assertEquals(input.name(), result.getName()),
                () -> assertEquals(input.description(), result.getDescription()),
                () -> assertEquals(createdBy, result.getCreatedBy()),
                () -> assertEquals(createdDate, result.getCreatedDate())
        );
    }
    //endregion
}
