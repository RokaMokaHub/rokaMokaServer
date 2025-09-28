package br.edu.ufpel.rokamoka.service.location;

import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author MauricioMucci
 */
@Service
@RequiredArgsConstructor
class LocationService implements ILocationService {

    private final LocationRepository locationRepository;

    @Override
    public LocationOutputDTO getLocation(Long id) throws RokaMokaContentNotFoundException {
        return this.locationRepository.findById(id)
                .map(LocationOutputDTO::new)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Localização não encontrada"));
    }

    @Override
    public List<LocationOutputDTO> getAllLocationsByAddress(Long addressId) {
        return this.locationRepository.findByEndereco_Id(addressId).stream().map(LocationOutputDTO::new).toList();
    }

    @Override
    public List<LocationOutputDTO> getAllLocations() {
        return this.locationRepository.findAll().stream().map(LocationOutputDTO::new).toList();
    }
}
