package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaForbiddenException;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Obra", description = "API para operações de CRUD em obras")
@RestController
@RequestMapping("/artwork")
@RequiredArgsConstructor
public class ArtworkController extends RokaMokaController {

    private final IArtworkService artworkService;
    private final ArtworkRepository artworkRepository;

    @Operation(summary = "Buscar obra por ID", description = "Retorna uma obra com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> findById(@PathVariable Long id) throws RokaMokaContentNotFoundException {
        Artwork artwork = artworkService.findById(id);
        return success(this.artworkRepository.createFullArtworkInfo(artwork.getId()));
    }

    @Operation(summary = "Upload de uma image em uma obra", description = "Faz upload de uma image, caso já exista estoura um erro")
    @PostMapping(value = "/upload/{artworkId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseWrapper<Void>> uploadImage(@PathVariable Long artworkId, @RequestParam("image") MultipartFile image) throws BadRequestException, RokaMokaForbiddenException, RokaMokaContentNotFoundException {
        if (image == null || image.isEmpty()) {
            throw new BadRequestException("É necessário enviar uma imagem");
        }
        this.artworkService.addImage(artworkId, image);
        return success();
    }

    @Operation(summary = "Criar nova obra", description = "Cria uma nova obra com os dados fornecidos")
    @PostMapping(path = "/{exhibitionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> create(@PathVariable Long exhibitionId,
                                                                       @ModelAttribute ArtworkInputDTO artworkDTO) throws RokaMokaContentNotFoundException {
        Artwork artwork = this.artworkService.create(exhibitionId, artworkDTO);
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
