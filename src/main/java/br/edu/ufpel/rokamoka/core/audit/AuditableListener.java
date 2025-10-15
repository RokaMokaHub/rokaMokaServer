package br.edu.ufpel.rokamoka.core.audit;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.security.UserAuthenticated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * A listener that automatically manages audit fields for entities implementing the {@code Auditable} interface.
 *
 * <p>This class listens to entity lifecycle events (such as creation and update) and updates fields such as
 * {@code createdBy}, {@code updatedBy}, {@code createdDate}, and {@code updatedDate}. It ensures that audit metadata is
 * populated consistently and systematically across the application.
 *
 * @author MauricioMucci
 * @see Auditable
 * @see PrePersist
 * @see PreUpdate
 */
public class AuditableListener {

    @PrePersist
    public void onCreate(Auditable auditable) {
        LocalDateTime now = LocalDateTime.now();
        auditable.setCreatedDate(now);
        auditable.setUpdatedDate(now);

        String username = getUsernameOrDefault();
        auditable.setCreatedBy(username);
        auditable.setUpdatedBy(username);
    }

    @PreUpdate
    public void onUpdate(Auditable auditable) {
        LocalDateTime now = LocalDateTime.now();
        auditable.setUpdatedDate(now);

        String username = getUsernameOrDefault();
        auditable.setUpdatedBy(username);
    }

    private static String getUsernameOrDefault() {
        ServiceContext context = ServiceContext.getContext();
        UserAuthenticated loggedInUser = context.getUser();
        String username = loggedInUser.getUsername();
        return StringUtils.isNotBlank(username) ? username : "system";
    }
}
