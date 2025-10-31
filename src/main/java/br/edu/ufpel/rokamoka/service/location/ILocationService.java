package br.edu.ufpel.rokamoka.service.location;

import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.location.input.LocationInputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.AddressOutputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Service interface for managing and retrieving information related to locations.
 *
 * @author MauricioMucci
 * @see LocationService
 */
@Validated
public interface ILocationService {

    LocationOutputDTO getLocation(@NotNull Long id);

    List<LocationOutputDTO> getAllLocationsByAddress(@NotNull Long addressId);

    List<LocationOutputDTO> getAllLocations();

    LocationOutputDTO create(@NotNull LocationInputDTO input);

    LocationOutputDTO update(@NotNull LocationInputDTO input);

    LocationOutputDTO delete(@NotNull Long id);

    List<AddressOutputDTO> getAllAddresses();

    Location getLocationOrElseThrow(Long id);
}
