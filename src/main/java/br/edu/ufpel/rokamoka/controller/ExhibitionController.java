package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionWithArtworksDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.exhibition.IExhibitionService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Exibição", description = "API para operações de CRUD em exposições")
@RestController
@RequestMapping("/exhibition")
@RequiredArgsConstructor
public class ExhibitionController extends RokaMokaController {

    private final IExhibitionService exhibitionService;

    @Operation(summary = "Buscar exibição por ID", description = "Retorna uma exibição com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> findById(@PathVariable Long id)
    throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.exhibitionService.findById(id);
        return this.success(new ExhibitionOutputDTO(exhibition));
    }

    @Operation(summary = "Criar nova exibição", description = "Cria uma nova exibição com os dados fornecidos")
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> create(@RequestBody ExhibitionInputDTO dto)
    throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.exhibitionService.save(dto);
        return this.success(new ExhibitionOutputDTO(exhibition));
    }

    @Operation(summary = "Atualizar exibição", description = "Atualiza os dados de uma exibição existente")
    @PostMapping("add/artwork/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionWithArtworksDTO>> addArtworks(@PathVariable Long id,
            @RequestBody List<ArtworkInputDTO> artworkInputDTOS) throws RokaMokaContentNotFoundException {
        ExhibitionWithArtworksDTO dto = this.exhibitionService.addArtworks(id, artworkInputDTOS);
        return this.success(dto);
    }

    @Operation(summary = "Deletar exibição", description = "Remove uma exibição com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> delete(@PathVariable Long id)
    throws RokaMokaContentNotFoundException {
        Exhibition exhibition = this.exhibitionService.findById(id);
        this.exhibitionService.deleteById(id);
        return this.success(new ExhibitionOutputDTO(exhibition));
    }
}
