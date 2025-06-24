package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.mokadex.output.MokadexOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.mokadex.IMokadexService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mauri
 */
@Tag(name = "Mokadex", description = "API para operações de CRUD em mokadex")
@RestController
@RequestMapping("/mokadex")
@RequiredArgsConstructor
public class MokadexController extends RokaMokaController {

    private final IMokadexService mokadexService;

    @PostMapping("/collect/{qrcode}")
    public ResponseEntity<ApiResponseWrapper<MokadexOutputDTO>> collectStar(
            @PathVariable(value = "qrcode") String qrCode
    ) throws RokaMokaContentNotFoundException {
        return success(mokadexService.collectStar(qrCode));
    }
}
