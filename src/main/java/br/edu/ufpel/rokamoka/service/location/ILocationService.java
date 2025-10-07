package br.edu.ufpel.rokamoka.service.location;

import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.location.input.LocationInputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.AddressOutputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
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

    LocationOutputDTO getLocation(@NotNull Long id) throws RokaMokaContentNotFoundException;

    List<LocationOutputDTO> getAllLocationsByAddress(@NotNull Long addressId);

    List<LocationOutputDTO> getAllLocations();

    LocationOutputDTO create(@NotNull LocationInputDTO input) throws RokaMokaContentDuplicatedException;

    LocationOutputDTO update(@NotNull LocationInputDTO input) throws RokaMokaContentNotFoundException;

    LocationOutputDTO delete(@NotNull Long id) throws RokaMokaContentNotFoundException;

    List<AddressOutputDTO> getAllAddresses();

    Location getLocationOrElseThrow(Long id) throws RokaMokaContentNotFoundException;
}
