package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;

import java.util.List;

public interface IExhibitionService {

    List<ExhibitionOutputDTO> getAllExhibitions();

    ExhibitionOutputDTO getExhibitionWithArtworks(Long id);

    ExhibitionOutputDTO create(ExhibitionInputDTO exhibition);

    ExhibitionOutputDTO delete(Long id);

    ExhibitionOutputDTO addArtworks(Long id, List<ArtworkInputDTO> inputList);

    Exhibition getExhibitionOrElseThrow(Long id);

    ExhibitionOutputDTO update(ExhibitionInputDTO dto);
}
