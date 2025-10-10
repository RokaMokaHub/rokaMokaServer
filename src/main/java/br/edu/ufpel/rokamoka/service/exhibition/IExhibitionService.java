package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;

import java.util.List;

public interface IExhibitionService {

    List<ExhibitionOutputDTO> getAllExhibitions();

    ExhibitionOutputDTO findById(Long id) throws RokaMokaContentNotFoundException;

    ExhibitionOutputDTO create(ExhibitionInputDTO exhibition) throws RokaMokaContentNotFoundException;

    ExhibitionOutputDTO delete(Long id) throws RokaMokaContentNotFoundException;

    ExhibitionOutputDTO addArtworks(Long id, List<ArtworkInputDTO> inputList) throws RokaMokaContentNotFoundException;

    Exhibition getExhibitionOrElseThrow(Long id) throws RokaMokaContentNotFoundException;

    ExhibitionOutputDTO update(ExhibitionInputDTO dto) throws RokaMokaContentNotFoundException;
}
