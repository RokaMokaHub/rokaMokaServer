package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.GroupValidators;
import br.edu.ufpel.rokamoka.dto.location.input.LocationInputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.location.ILocationService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
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

/**
 * REST Controller providing endpoints for CRUD operations on the location resource.
 *
 * @author MauricioMucci
 * @see RokaMokaController
 * @see ILocationService
 */
@Tag(name = "Local", description = "API para gerenciamento de locais e seus endereços")
@Validated
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
class LocationController extends RokaMokaController {

    private final ILocationService locationService;

    @Operation(summary = "Buscar local por id", description = "Retorna um local com base no ID informado")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Local encontrado"),
            @ApiResponse(responseCode = "404", description = "Local não encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> getLocation(@PathVariable @NotNull Long id)
    throws RokaMokaContentNotFoundException {
        LocationOutputDTO location = this.locationService.getLocation(id);
        return this.success(location);
    }

    @Operation(summary = "Buscar todos os locais",
            description = "Retorna uma lista contendo todos os locais do sistema")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de todos os locais")})
    @GetMapping("/all")
    public ResponseEntity<ApiResponseWrapper<List<LocationOutputDTO>>> getAllLocations() {
        List<LocationOutputDTO> locations = this.locationService.getAllLocations();
        return this.success(locations);
    }

    @Operation(summary = "Buscar todos os locais de um determinado endereço",
            description = "Retorna uma lista contendo todos os locais de um determinado endereço")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Lista de todos os locais")})
    @GetMapping("/address/{addressId}")
    public ResponseEntity<ApiResponseWrapper<List<LocationOutputDTO>>> getAllLocationsByAddress(
            @PathVariable @NotNull Long addressId) {
        List<LocationOutputDTO> locations = this.locationService.getAllLocationsByAddress(addressId);
        return this.success(locations);
    }

    @Operation(summary = "Cadastrar novo local", description = "Cadastra um novo local com os dados fornecidos")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Local cadastrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Local duplicado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao cadastrar local")})
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> register(
            @RequestBody @Validated(value = GroupValidators.Create.class) @NotNull LocationInputDTO input)
    throws RokaMokaContentDuplicatedException {
        LocationOutputDTO location = this.locationService.create(input);
        return this.success(location);
    }

    @Operation(summary = "Atualizar local existente",
            description = "Atualiza um local existente com os dados fornecidos")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Local atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Local não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao atualizar local")})
    @PatchMapping
    public ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> patch(
            @RequestBody @Validated(value = GroupValidators.Update.class) @NotNull LocationInputDTO input)
    throws RokaMokaContentNotFoundException {
        LocationOutputDTO location = this.locationService.update(input);
        return this.success(location);
    }

    @Operation(summary = "Remover local existente", description = "Remove um local existente com base no ID fornecido")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Local removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Local não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro inesperado ao remover local")})
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> remove(@PathVariable @NotNull Long id)
    throws RokaMokaContentNotFoundException {
        LocationOutputDTO location = this.locationService.delete(id);
        return this.success(location);
    }
}
