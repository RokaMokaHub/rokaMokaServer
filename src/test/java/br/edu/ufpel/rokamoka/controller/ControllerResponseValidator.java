package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author MauricioMucci
 */
public interface ControllerResponseValidator<T> {

    default void assertExpectedResponse(ResponseEntity<ApiResponseWrapper<T>> response, T expected) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<T> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        T actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertEquals(expected, actual, "Output atual deve conter atributos esperados");
    }

    default void assertVoidResponse(ResponseEntity<ApiResponseWrapper<T>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<T> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        T actual = body.getBody();
        assertNull(actual, "Output atual deve ser null");
    }

    default void assertCollectionResponse(ResponseEntity<ApiResponseWrapper<Collection<T>>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<Collection<T>> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        Collection<T> actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertFalse(actual.isEmpty(), "Output atual deve conter pelo menos um elemento");
        actual.forEach(Assertions::assertNotNull);
    }

    default void assertEmptyCollectionResponse(ResponseEntity<ApiResponseWrapper<Collection<T>>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<Collection<T>> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        Collection<T> actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertTrue(actual.isEmpty(), "Output atual deve estar vazio");
    }
}
