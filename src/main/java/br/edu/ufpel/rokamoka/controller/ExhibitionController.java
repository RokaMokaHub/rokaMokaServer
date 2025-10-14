package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.GroupValidators.Create;
import br.edu.ufpel.rokamoka.dto.GroupValidators.Update;
import br.edu.ufpel.rokamoka.dto.artwork.input.ArtworkInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.exhibition.IExhibitionService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Exposição", description = "API para operações de CRUD em exposições")
@Validated
@RestController
@RequestMapping("/exhibition")
@RequiredArgsConstructor
public class ExhibitionController extends RokaMokaController {

    private final IExhibitionService exhibitionService;

    @Operation(summary = "Buscar exposição por ID",
            description = "Retorna os detalhes de uma exposição específica com base no seu ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Exposição encontrada"),
            @ApiResponse(responseCode = "404", description = "Exposição não encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> getExhibition(@PathVariable Long id)
    throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.getExhibitionWithArtworks(id);
        return this.success(exhibition);
    }

    @Operation(summary = "Listar todas as exposições",
            description = "Retorna uma lista com todas as exposições cadastradas no sistema.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de todas as exposições")})
    @GetMapping("/all")
    public ResponseEntity<ApiResponseWrapper<List<ExhibitionOutputDTO>>> getAllExhibitions() {
        List<ExhibitionOutputDTO> exhibitions = this.exhibitionService.getAllExhibitions();
        return this.success(exhibitions);
    }

    @Operation(summary = "Cadastrar uma nova exposição",
            description = "Cria o registro de uma nova exposição a partir dos dados fornecidos.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Exposição cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Exposição duplicada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao cadastrar exposição")})
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> register(
            @RequestBody @Validated(value = Create.class) ExhibitionInputDTO dto)
    throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.create(dto);
        return this.success(exhibition);
    }

    @Operation(summary = "Atualizar uma exposição",
            description = "Atualiza o registro de uma exposição a partir dos dados fornecidos.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Exposição atualizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao atualizar exposição")})
    @PatchMapping
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> patch(
            @RequestBody @Validated(value = Update.class) ExhibitionInputDTO dto)
    throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.update(dto);
        return this.success(exhibition);
    }

    @Operation(summary = "Adicionar obras a uma exposição",
            description = """
                          Adiciona uma lista de novas obras de arte a uma exposição existente, criando-as e \
                          associando-as à exposição informada.""")
    @PostMapping("add/artwork/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> addArtworks(
            @PathVariable Long id,
            @RequestBody List<ArtworkInputDTO> artworks) throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.addArtworks(id, artworks);
        return this.success(exhibition);
    }

    @Operation(summary = "Remover uma exposição",
            description = "Remove o registro de uma exposição com base no ID informado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> remove(@PathVariable Long id)
    throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.delete(id);
        return this.success(exhibition);
    }
}
