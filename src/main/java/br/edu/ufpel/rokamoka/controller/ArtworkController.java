package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.input.ImageUploadDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Obra", description = "API para operações de CRUD em obras")
@RestController
@RequestMapping("/artwork")
@RequiredArgsConstructor
public class ArtworkController extends RokaMokaController {

    private final IArtworkService artworkService;

    @Operation(summary = "Buscar obra por ID", description = "Retorna uma obra com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> findById(@PathVariable Long id) throws RokaMokaContentNotFoundException {
        Artwork artwork = artworkService.findById(id);
        return success(new ArtworkOutputDTO(artwork));
    }

    @Operation(summary = "Upload de uma image em uma obra", description = "Faz upload de uma image, caso já exista estoura um erro")
    @PostMapping("/upload/{artworkId}")
    public ResponseEntity<ApiResponseWrapper<Void>> uploadImage(@PathVariable Long artworkId, @RequestBody ImageUploadDTO imageDTO) throws BadRequestException, RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        if (imageDTO.image() == null || imageDTO.image().isEmpty()) {
            throw new BadRequestException("É necessário enviar uma imagem");
        }
        this.artworkService.addImage(artworkId, imageDTO.image());
        return success();
    }

    @Operation(summary = "Criar nova obra", description = "Cria uma nova obra com os dados fornecidos")
    @PostMapping("/{exhibitionId}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> create(@PathVariable Long exhibitionId, @RequestBody @Valid ArtworkInputDTO artworkDTO) throws RokaMokaContentNotFoundException {
        Artwork artwork = artworkService.create(exhibitionId, artworkDTO);
        return success(new ArtworkOutputDTO(artwork));
    }

    @Operation(summary = "Deletar obra", description = "Remove uma obra com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> deletar(@PathVariable Long id) throws RokaMokaContentNotFoundException {
        var artwork = artworkService.findById(id);
        artworkService.deleteById(id);
        return success(new ArtworkOutputDTO(artwork));
    }
}
