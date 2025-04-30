package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.ArtworkService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Obra", description = "API para operações de CRUD em obras")
@RestController
@RequestMapping("/artwork")
@RequiredArgsConstructor
public class ArtworkController extends RokaMokaController {

    private final ArtworkService artworkService;

    @Operation(summary = "Buscar obra por ID", description = "Retorna uma obra com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> findById(@PathVariable Long id) {
        Artwork artwork = artworkService.findById(id).orElseThrow();
        return success(new ArtworkOutputDTO(artwork));
    }

    @Operation(summary = "Criar nova obra", description = "Cria uma nova obra com os dados fornecidos")
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> create(@RequestBody ArtworkInputDTO artworkDTO) {
        Artwork artwork = artworkService.save(Artwork.builder().nome(artworkDTO.nome()).build());
        return success(new ArtworkOutputDTO(artwork));
    }

    @Operation(summary = "Atualizar obra", description = "Atualiza os dados de uma obra existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> update(@PathVariable Long id,
                                                                       @RequestBody ArtworkInputDTO updatedArtworkDTO) throws RokaMokaContentNotFoundException {
        Artwork artwork = artworkService.findById(id)
                .orElseThrow(RokaMokaContentNotFoundException::new);
        artwork.setNome(updatedArtworkDTO.nome());
        return success(new ArtworkOutputDTO(this.artworkService.save(artwork)));
    }

    @Operation(summary = "Deletar obra", description = "Remove uma obra com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<Void>> deletar(@PathVariable Long id) {
        if (artworkService.findById(id).isPresent()) {
            artworkService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
