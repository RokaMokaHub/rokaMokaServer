package br.edu.ufpel.rokamoka.core.audit;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.service.MockUserSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;

/**
 *
 * @author MauricioMucci
 */
@ExtendWith(MockitoExtension.class)
class AuditableListenerTest implements MockUserSession {

    @InjectMocks private AuditableListener auditableListener;

    //region onCreate
    @Test
    void onCreate_shouldFillOutAuditableFields_whenContextUserIsPresent() {
        // Arrange
        User user = new User();
        User spyUser = spy(user);

        ServiceContext mockContext = this.mockServiceContext();

        // Act
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            this.auditableListener.onCreate(spyUser);
        }

        // Assert
        assertEquals(LOGGED_USER_NAME, spyUser.getCreatedBy());
        assertEquals(LOGGED_USER_NAME, spyUser.getUpdatedBy());
        assertNotNull(spyUser.getCreatedDate());
        assertNotNull(spyUser.getUpdatedDate());
    }

    @Test
    void onCreate_shouldDefault_whenContextUserIsEmpty() {
        // Arrange
        User user = new User();
        User spyUser = spy(user);

        ServiceContext mockContext = this.mockBlankServiceContext();

        // Act
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            this.auditableListener.onCreate(spyUser);
        }

        // Assert
        assertEquals("system", spyUser.getCreatedBy());
        assertEquals("system", spyUser.getUpdatedBy());
        assertNotNull(spyUser.getCreatedDate());
        assertNotNull(spyUser.getUpdatedDate());
    }
    //endregion

    //region onUpdate
    @Test
    void onUpdate_shouldFillOutAuditableFields_whenContextUserIsPresent() {
        // Arrange
        User user = new User();
        User spyUser = spy(user);

        ServiceContext mockContext = this.mockServiceContext();

        // Act
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            this.auditableListener.onUpdate(spyUser);
        }

        // Assert
        assertEquals(LOGGED_USER_NAME, spyUser.getUpdatedBy());
        assertNotNull(spyUser.getUpdatedDate());
    }

    @Test
    void onUpdate_shouldDefault_whenContextUserIsEmpty() {
        // Arrange
        User user = new User();
        User spyUser = spy(user);

        ServiceContext mockContext = this.mockBlankServiceContext();

        // Act
        try (MockedStatic<ServiceContext> mockedServiceContext = mockStatic(ServiceContext.class)) {
            mockedServiceContext.when(ServiceContext::getContext).thenReturn(mockContext);

            this.auditableListener.onUpdate(spyUser);
        }

        // Assert
        assertEquals("system", spyUser.getUpdatedBy());
        assertNotNull(spyUser.getUpdatedDate());
    }
    //endregion
}
