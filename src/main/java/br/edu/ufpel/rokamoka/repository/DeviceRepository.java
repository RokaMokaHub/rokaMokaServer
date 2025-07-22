package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
