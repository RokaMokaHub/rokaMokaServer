package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Emblem;
import br.edu.ufpel.rokamoka.dto.emblem.input.EmblemInputDTO;
import br.edu.ufpel.rokamoka.dto.emblem.output.EmblemOutputDTO;
import br.edu.ufpel.rokamoka.service.emblem.IEmblemService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@Tag(name = "Emblema", description = "API para operações de CRUD em emblema")
@RestController
@RequestMapping("/emblem")
public class EmblemRestController extends RokaMokaController {

    private final IEmblemService emblemService;

    @Operation(summary = "Buscar emblema por ID", description = "Retorna um emblema com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<EmblemOutputDTO>> findById(@PathVariable Long id) {
        Emblem emblem = this.emblemService.findById(id);
        return this.success(new EmblemOutputDTO(emblem));
    }

    @Operation(summary = "Criar novo emblema", description = "Cria um novo emblema com os dados fornecidos")
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<EmblemOutputDTO>> register(@RequestBody EmblemInputDTO emblemInputDTO) {
        Emblem emblem = this.emblemService.create(emblemInputDTO);
        return this.success(new EmblemOutputDTO(emblem));
    }

    @Operation(summary = "Deletar emblema", description = "Remove um emblema com base no ID informado")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<EmblemOutputDTO>> remove(@PathVariable Long id) {
        Emblem emblem = this.emblemService.delete(id);
        return this.success(new EmblemOutputDTO(emblem));
    }
}
