package br.edu.ufpel.rokamoka.service.location;

import br.edu.ufpel.rokamoka.core.Address;
import br.edu.ufpel.rokamoka.core.Location;
import br.edu.ufpel.rokamoka.dto.location.input.AddressInputDTO;
import br.edu.ufpel.rokamoka.dto.location.input.LocationInputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.AddressOutputDTO;
import br.edu.ufpel.rokamoka.dto.location.output.LocationOutputDTO;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentDuplicatedException;
import br.edu.ufpel.rokamoka.exceptions.RokaMokaContentNotFoundException;
import br.edu.ufpel.rokamoka.repository.AddressRepository;
import br.edu.ufpel.rokamoka.repository.LocationRepository;
import br.edu.ufpel.rokamoka.utils.location.AddressBuilder;
import br.edu.ufpel.rokamoka.utils.location.LocationBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * Service implementation of the {@link ILocationService} interface for managing operations on the {@link Location}
 * resource.
 *
 * @author MauricioMucci
 * @see LocationRepository
 */
@Validated
@Service
@RequiredArgsConstructor
class LocationService implements ILocationService {

    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;

    @Override
    public LocationOutputDTO getLocation(@NotNull Long id) throws RokaMokaContentNotFoundException {
        Location location = this.getLocationOrElseThrow(id);
        return toOutput(location);
    }

    private static LocationOutputDTO toOutput(Location location) {
        return new LocationOutputDTO(location);
    }

    @Override
    public List<LocationOutputDTO> getAllLocationsByAddress(@NotNull Long addressId) {
        return this.locationRepository.findAllByEndereco_Id(addressId).stream().map(LocationOutputDTO::new).toList();
    }

    @Override
    public List<LocationOutputDTO> getAllLocations() {
        return this.locationRepository.findAll().stream().map(LocationOutputDTO::new).toList();
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public LocationOutputDTO create(@NotNull LocationInputDTO input) throws RokaMokaContentDuplicatedException {
        if (this.locationRepository.existsByNome(input.nome())) {
            throw new RokaMokaContentDuplicatedException("Localização já existe");
        }

        Address address = this.getByAtributesIn(input.endereco())
                .orElseGet(() -> new AddressBuilder(input.endereco()).build());

        Location location = new LocationBuilder(input, address).build();
        location = this.locationRepository.save(location);

        return toOutput(location);
    }

    private Optional<Address> getByAtributesIn(AddressInputDTO input) {
        return StringUtils.isBlank(input.complemento())
                ? this.addressRepository.findByRuaAndNumeroAndCep(input.rua(), input.numero(), input.cep())
                : this.addressRepository.findByRuaAndNumeroAndCepAndComplemento(input.rua(), input.numero(), input.cep(), input.complemento());
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public LocationOutputDTO update(@NotNull LocationInputDTO input) throws RokaMokaContentNotFoundException {
        Location location = this.getLocationOrElseThrow(input.id());
        Address address = location.getEndereco();

        AddressInputDTO addressDTO = input.endereco();
        if (addressDTO != null) {
            address = new AddressBuilder(address.getId(), addressDTO).build();
        }

        location = new LocationBuilder(location.getId(), input, address).build();
        location = this.locationRepository.save(location);

        return toOutput(location);
    }

    @Override
    @Transactional(propagation = REQUIRED)
    public LocationOutputDTO delete(@NotNull Long id) throws RokaMokaContentNotFoundException {
        Location location = this.getLocationOrElseThrow(id);
        this.locationRepository.delete(location);
        return toOutput(location);
    }

    @Override
    public List<AddressOutputDTO> getAllAddresses() {
        return this.addressRepository.findAll().stream().map(AddressOutputDTO::new).toList();
    }

    @Override
    public Location getLocationOrElseThrow(Long id) throws RokaMokaContentNotFoundException {
        return this.locationRepository.findById(id)
                .orElseThrow(() -> new RokaMokaContentNotFoundException("Localização não encontrada: ID=" + id));
    }
}
