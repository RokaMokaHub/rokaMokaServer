package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Exhibition;
import br.edu.ufpel.rokamoka.dto.exhibition.input.ExhibitionInputDTO;
import br.edu.ufpel.rokamoka.dto.exhibition.output.ExhibitionOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.IExhibitionService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Exibição", description = "API para operações de CRUD em exposições")
@RestController
@RequestMapping("/exhibition")
@RequiredArgsConstructor
public class ExhibitionController extends RokaMokaController {

    private final IExhibitionService IExhibitionService;

    @Operation(summary = "Buscar exibição por ID", description = "Retorna uma exibição com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> findById(@PathVariable Long id) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = IExhibitionService.findById(id);
        return success(new ExhibitionOutputDTO(exhibition));
    }

    @Operation(summary = "Criar nova exibição", description = "Cria uma nova exibição com os dados fornecidos")
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> create(@RequestBody ExhibitionInputDTO dto) throws RokaMokaContentNotFoundException {
        return success(new ExhibitionOutputDTO(IExhibitionService.save(dto)));
    }

    @Operation(summary = "Atualizar exibição", description = "Atualiza os dados de uma exibição existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> update(@PathVariable Long id,
                                                                          @RequestBody ExhibitionInputDTO dto) throws RokaMokaContentNotFoundException {
        return success(new ExhibitionOutputDTO(IExhibitionService.save(id, dto)));
    }

    @Operation(summary = "Deletar exibição", description = "Remove uma exibição com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<ExhibitionOutputDTO>> delete(@PathVariable Long id) throws RokaMokaContentNotFoundException {
        Exhibition exhibition = IExhibitionService.findById(id);
        IExhibitionService.deleteById(id);
        return success(new ExhibitionOutputDTO(exhibition));
    }
}
