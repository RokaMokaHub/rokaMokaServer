package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.ArtworkService;
import br.edu.ufpel.rokamoka.service.ExhibitionService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Exibição", description = "API para operações de CRUD em exposições")
@RestController
@RequestMapping("/exhibition")
@RequiredArgsConstructor
public class ExhibitionController extends RokaMokaController {

    private final ExhibitionService exhibitionService;
    private final ArtworkService artworkService;

    @Operation(summary = "Buscar exibição por ID", description = "Retorna uma exibição com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> findById(@PathVariable Long id) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = exhibitionService.findById(id).orElseThrow(RokaMokaContentNotFoundException::new);
        return success(new ExhibitionOutputDTO(exhibition));
    }

    @Operation(summary = "Criar nova exibição", description = "Cria uma nova exibição com os dados fornecidos")
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> create(@RequestBody ExhibitionInputDTO dto) {
        Exhibition exhibition = Exhibition.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
        List<Artwork> artworks = dto.artworks().stream().map(a -> new Artwork(a, exhibition)).collect(Collectors.toList());
        return success(new ExhibitionOutputDTO(exhibitionService.save(exhibition, artworks)));
    }

    @Operation(summary = "Atualizar exibição", description = "Atualiza os dados de uma exibição existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> update(@PathVariable Long id,
                                                                          @RequestBody ExhibitionInputDTO dto) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = exhibitionService.findById(id)
                .orElseThrow(RokaMokaContentNotFoundException::new);
        exhibition.setName(dto.name());
        exhibition.setDescription(dto.description());
        List<Artwork> artworks = dto.artworks().stream().map(a -> new Artwork(a, exhibition)).collect(Collectors.toList());
        return success(new ExhibitionOutputDTO(exhibitionService.save(exhibition, artworks)));
    }

    @Operation(summary = "Deletar exibição", description = "Remove uma exibição com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<Void>> delete(@PathVariable Long id) {
        if (exhibitionService.findById(id).isPresent()) {
            exhibitionService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
