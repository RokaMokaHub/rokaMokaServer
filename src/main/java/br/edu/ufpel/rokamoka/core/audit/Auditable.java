package br.edu.ufpel.rokamoka.core.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A base class to provide auditable fields and behavior for persistent entities.
 *
 * <ul>
 *   <li>Tracks the user who created the entity.</li>
 *   <li>Tracks the user who last modified the entity.</li>
 *   <li>Records the timestamps for creation and last modification.</li>
 * </ul>
 *
 * <p>Entities extending this class need to inherit its properties and ensure proper configuration in the persistence
 * context.
 *
 * @author MauricioMucci
 * @see AuditableListener
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditableListener.class)
public abstract class Auditable {

    @Column(name = "created_by", nullable = false) private String createdBy;
    @Column(name = "updated_by", nullable = false) private String updatedBy;
    @Column(name = "created_date", nullable = false) private LocalDateTime createdDate;
    @Column(name = "updated_date", nullable = false) private LocalDateTime updatedDate;
}
