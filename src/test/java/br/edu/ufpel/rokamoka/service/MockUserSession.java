package br.edu.ufpel.rokamoka.service;

import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.security.UserAuthenticated;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author MauricioMucci
 */
public interface MockUserSession {

    String LOGGED_USER_NAME = "loggedUserName";

    default ServiceContext mockServiceContext() {
        ServiceContext mockContext = mock(ServiceContext.class);
        UserAuthenticated mockUserAuthenticated = mock(UserAuthenticated.class);
        when(mockContext.getUser()).thenReturn(mockUserAuthenticated);
        when(mockUserAuthenticated.getUsername()).thenReturn(LOGGED_USER_NAME);
        return mockContext;
    }

    default ServiceContext mockBlankServiceContext() {
        ServiceContext mockContext = mock(ServiceContext.class);
        UserAuthenticated mockUserAuthenticated = mock(UserAuthenticated.class);
        when(mockContext.getUser()).thenReturn(mockUserAuthenticated);
        when(mockUserAuthenticated.getUsername()).thenReturn("");
        return mockContext;
    }
}
