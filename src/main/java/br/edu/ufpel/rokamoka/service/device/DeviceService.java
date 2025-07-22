package br.edu.ufpel.rokamoka.service.device;

import br.edu.ufpel.rokamoka.core.Device;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.repository.DeviceRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Service implementation of the {@link IDeviceService} interface for managing operations on the {@link Device}
 * resource.
 *
 * @author MauricioMucci
 * @see DeviceRepository
 */
@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class DeviceService implements IDeviceService {

    private final DeviceRepository deviceRepository;

    @Override
    public Device save(@NotBlank String deviceId, User user) {
        log.info("Salvando [{}] para o [{}] informado", Device.class.getSimpleName(), user);
        var device = Device.builder()
                .deviceId(deviceId)
                .user(user)
                .build();
        return this.deviceRepository.save(device);
    }
}
