package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.service.location.ILocationService;
import br.edu.ufpel.rokamoka.wrapper.RokaMokaController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author MauricioMucci
 */
@Tag(name = "Local", description = "API para gerenciamento de locais e seus endereços")
@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
class LocationController extends RokaMokaController {

    private final ILocationService locationService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<LocationOutputDTO>> getLocation(@PathVariable Long id)
    throws RokaMokaContentNotFoundException {
        LocationOutputDTO location = this.locationService.getLocation(id);
        return this.success(location);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponseWrapper<List<LocationOutputDTO>>> getAllLocations() {
        List<LocationOutputDTO> locations = this.locationService.getAllLocations();
        return this.success(locations);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<ApiResponseWrapper<List<LocationOutputDTO>>> getAllLocationsByAddress(@PathVariable Long addressId) {
        List<LocationOutputDTO> locations = this.locationService.getAllLocationsByAddress(addressId);
        return this.success(locations);
    }

    @PostMapping
    public ResponseEntity<ApiResponseWrapper<Void>> register() {
        return this.success();
    }

    @PatchMapping
    public ResponseEntity<ApiResponseWrapper<Void>> patch() {
        return this.success();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<Void>> remove(@PathVariable String id) {
        return this.success();
    }

}
