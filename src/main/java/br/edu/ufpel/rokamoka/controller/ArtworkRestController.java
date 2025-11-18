package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Artwork;
import br.edu.ufpel.rokamoka.dto.GroupValidators.Create;
import br.edu.ufpel.rokamoka.dto.GroupValidators.Update;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.repository.ArtworkRepository;
import br.edu.ufpel.rokamoka.service.artwork.IArtworkService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RequiredArgsConstructor
@Tag(name = "Obra", description = "API para operações de CRUD em obras")
@RestController
@RequestMapping("/artwork")
public class ArtworkRestController extends RokaMokaController {

    private final IArtworkService artworkService;
    private final ArtworkRepository artworkRepository;

    @Operation(summary = "Buscar obra por ID",
            description = "Retorna os detalhes de uma obra específica com base no seu ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Obra encontrada"),
            @ApiResponse(responseCode = "404", description = "Obra não encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> findById(@PathVariable Long id) {
        Artwork artwork = this.artworkService.getArtworkOrElseThrow(id);
        ArtworkOutputDTO dto = this.artworkRepository.createFullArtworkInfo(artwork.getId());
        return this.success(dto);
    }

    @Operation(summary = "Buscar obras por ID da exposição",
            description = "Retorna todas as obras de uma determinada exposição.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Obras encontradas")})
    @GetMapping("/exposicao/{exhibitionId}")
    public ResponseEntity<ApiResponseWrapper<List<ArtworkOutputDTO>>> getAllByExhibitionId(
            @PathVariable Long exhibitionId) {
        List<Artwork> artworks = this.artworkService.getAllArtworkByExhibitionId(exhibitionId);
        return this.success(artworks.stream().map(ArtworkOutputDTO::new).toList());
    }

    @Operation(summary = "Buscar obra por QR Code",
            description = "Retorna os detalhes de uma obra específica com base no seu QR code.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Obra encontrada"),
            @ApiResponse(responseCode = "404", description = "Obra não encontrada")})
    @GetMapping("/qrcode/{qrcode}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> getArtworkByQrCode(@PathVariable String qrcode) {
        Artwork artwork = this.artworkService.getByQrCodeOrThrow(qrcode);
        ArtworkOutputDTO dto = this.artworkRepository.createFullArtworkInfo(artwork.getId());
        return this.success(dto);
    }

    @Operation(summary = "Upload de uma image em uma obra",
            description = "Faz upload de uma image, caso já exista estoura um erro")
    @PostMapping(value = "/upload/{artworkId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseWrapper<Void>> uploadImage(
            @PathVariable Long artworkId,
            @RequestParam("image") MultipartFile image) throws BadRequestException {
        if (image == null || image.isEmpty()) {
            throw new BadRequestException("É necessário enviar uma imagem");
        }
        this.artworkService.addImage(artworkId, image);
        return this.success();
    }

    @Operation(summary = "Cadastrar uma nova obra",
            description = "Cria o registro de uma nova obra a partir dos dados fornecidos.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Obra cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "404", description = "Exposição não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao cadastrar obra")})
    @PostMapping(path = "/{exhibitionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> register(
            @PathVariable Long exhibitionId,
            @ModelAttribute @Validated(value = Create.class) ArtworkInputDTO artworkDTO) {
        Artwork artwork = this.artworkService.create(exhibitionId, artworkDTO);
        return this.success(new ArtworkOutputDTO(artwork));
    }

    @Operation(summary = "Atualizar uma obra",
            description = "Atualiza o registro de uma obra a partir dos dados fornecidos.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Obra atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
            @ApiResponse(responseCode = "404", description = "Obra não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao atualizar obra")})
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> patch(
            @ModelAttribute @Validated(value = Update.class) ArtworkInputDTO input) {
        ArtworkOutputDTO artwork = this.artworkService.update(input);
        return this.success(artwork);
    }

    @Operation(summary = "Remover uma obra", description = "Remove o registro de uma obra com base no ID informado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ArtworkOutputDTO>> remove(@PathVariable Long id) {
        ArtworkOutputDTO artwork = this.artworkService.delete(id);
        return this.success(artwork);
    }
}
