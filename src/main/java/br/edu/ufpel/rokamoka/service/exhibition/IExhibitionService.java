package br.edu.ufpel.rokamoka.service.exhibition;

import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionWithArtworksDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;

import java.util.List;

public interface IExhibitionService {

    List<Exhibition> findAll();

    Exhibition findById(Long id) throws RokaMokaContentNotFoundException;

    Exhibition save(ExhibitionInputDTO exhibition);

    void deleteById(Long id);

    ExhibitionWithArtworksDTO addArtworks(Long exhibitionId, List<ArtworkInputDTO> artworkInputDTOS) throws RokaMokaContentNotFoundException;
}
