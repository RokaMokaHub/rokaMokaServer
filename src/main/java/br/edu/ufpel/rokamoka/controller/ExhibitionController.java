package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.GroupValidators;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Exibição", description = "API para operações de CRUD em exposições")
@Validated
@RestController
@RequestMapping("/exhibition")
@RequiredArgsConstructor
public class ExhibitionController extends RokaMokaController {

    private final IExhibitionService exhibitionService;

    @Operation(summary = "Buscar exposição por id", description = "Retorna uma exposição com base no ID informado")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Exposição encontrada"),
            @ApiResponse(responseCode = "404", description = "Exposição não encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> getExhibition(@PathVariable Long id)
    throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.findById(id);
        return this.success(exhibition);
    }

    @Operation(summary = "Buscar todos as exposições",
            description = "Retorna uma lista contendo todos as exposições do sistema")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de todas as exposições")})
    @GetMapping("/all")
    public ResponseEntity<ApiResponseWrapper<List<ExhibitionOutputDTO>>> getAllExhibitions() {
        List<ExhibitionOutputDTO> exhibitions = this.exhibitionService.getAllExhibitions();
        return this.success(exhibitions);
    }

    @Operation(summary = "Cadastrar nova exposição",
            description = "Cadastra uma nova exposição com os dados fornecidos")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Exposição cadastrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exposição duplicada"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao cadastrar exposição")})
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> register(
            @RequestBody @Validated(value = GroupValidators.Create.class) ExhibitionInputDTO dto)
    throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.create(dto);
        return this.success(exhibition);
    }

    @Operation(summary = "Atualizar obras da exposição", description = "Atualiza os dados de uma exposição existente")
    @PostMapping("add/artwork/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> addArtworks(@PathVariable Long id,
            @RequestBody List<ArtworkInputDTO> artworks) throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.addArtworks(id, artworks);
        return this.success(exhibition);
    }

    @Operation(summary = "Deletar exibição", description = "Remove uma exibição com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> remove(@PathVariable Long id)
    throws RokaMokaContentNotFoundException {
        ExhibitionOutputDTO exhibition = this.exhibitionService.delete(id);
        return this.success(exhibition);
    }
}
