package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.core.Mokadex;
import br.edu.ufpel.rokamoka.dto.artwork.output.ArtworkOutputDTO;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.mokadex.IMokadexService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST Controller providing endpoints for CRUD operations on the {@link Mokadex} resource.
 *
 * @author MauricioMucci
 * @see RokaMokaController
 * @see IMokadexService
 */
@Tag(name = "Mokadex", description = "API para operações de CRUD em mokadex")
@RestController
@RequestMapping("/mokadex")
@RequiredArgsConstructor
public class MokadexController extends RokaMokaController {

    private final IMokadexService mokadexService;

    @Operation(
            summary = "Endpoint para adicionar obras/estrelas ao mokadex",
            description = """
                          Operação para ler um qr code, referente à uma obra, e ao identificar a mesma adicionar ao \
                          mokadex do usuário logado"""
    )
    @PostMapping(value = "/collect/{qrcode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<MokadexOutputDTO>> collectStar(
            @PathVariable(value = "qrcode") String qrCode)
            throws RokaMokaContentNotFoundException, RokaMokaContentDuplicatedException {
        Mokadex mokadex = this.mokadexService.collectStar(qrCode);
        MokadexOutputDTO output = this.mokadexService.getMokadexOutputDTOByMokadex(mokadex);
        return this.success(output);
    }

    @Operation(
            summary = "Endpoint para descobrir obras/estrelas não coletadas",
            description = """
                          Operação para ler um ID de exposição, e listar todas as suas respectivas obras que ainda \
                          não foram coletadas pelo usuário logado"""
    )
    @GetMapping("/missing/{exhibitionId}")
    public ResponseEntity<ApiResponseWrapper<Set<ArtworkOutputDTO>>> findMissingStarsByExhibition(
            @PathVariable Long exhibitionId) throws RokaMokaContentNotFoundException {
        Set<ArtworkOutputDTO> output = this.mokadexService.getMissingStarsByExhibition(exhibitionId)
                .stream()
                .map(ArtworkOutputDTO::new)
                .collect(Collectors.toUnmodifiableSet());
        return this.success(output);
    }
}
