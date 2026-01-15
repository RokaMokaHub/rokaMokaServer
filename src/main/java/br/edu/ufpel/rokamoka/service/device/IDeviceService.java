package br.edu.ufpel.rokamoka.service.device;

import br.edu.ufpel.rokamoka.core.Device;
import br.edu.ufpel.rokamoka.core.User;
import jakarta.validation.constraints.NotBlank;

/**
 * Service interface for managing and retrieving information related to {@link Device}.
 *
 * @author MauricioMucci
 * @see DeviceService
 */
public interface IDeviceService {

    Device save(@NotBlank String deviceId, User user);
}
