package br.edu.ufpel.rokamoka.service.location;

import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;

import java.util.List;

/**
 *
 * @author MauricioMucci
 */
public interface ILocationService {

    LocationOutputDTO getLocation(Long id) throws RokaMokaContentNotFoundException;

    List<LocationOutputDTO> getAllLocationsByAddress(Long addressId);

    List<LocationOutputDTO> getAllLocations();
}
