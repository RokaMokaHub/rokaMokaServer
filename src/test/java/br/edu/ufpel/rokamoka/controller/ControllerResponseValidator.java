package br.edu.ufpel.rokamoka.controller;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This utility provides a set of default methods to assert various conditions for responses returned by controllers in
 * a standardized manner. It simplifies the testing of response structures by performing common assertions on the HTTP
 * status, response body, and the contents of that body.
 *
 * @author MauricioMucci
 * @see ApiResponseWrapper
 * @see ResponseEntity
 */
public interface ControllerResponseValidator {

    default <T> void assertExpectedResponse(ResponseEntity<ApiResponseWrapper<T>> response, T expected) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<T> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        T actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertEquals(expected, actual, "Output atual deve conter atributos esperados");
    }

    default <T> void assertVoidResponse(ResponseEntity<ApiResponseWrapper<T>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<T> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        T actual = body.getBody();
        assertNull(actual, "Output atual deve ser null");
    }

    default <T> void assertListResponse(ResponseEntity<ApiResponseWrapper<List<T>>> response, List<T> expected) {
        if (expected.isEmpty()) {
            this.assertEmptyListResponse(response);
        } else {
            this.assertListResponse(response);
        }
    }

    default <T> void assertListResponse(ResponseEntity<ApiResponseWrapper<List<T>>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<List<T>> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        List<T> actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertFalse(actual.isEmpty(), "Output atual deve conter pelo menos um elemento");
        actual.forEach(Assertions::assertNotNull);
    }

    default <T> void assertEmptyListResponse(ResponseEntity<ApiResponseWrapper<List<T>>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<List<T>> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        List<T> actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertTrue(actual.isEmpty(), "Output atual deve estar vazio");
    }

    default <T> void assertSetResponse(ResponseEntity<ApiResponseWrapper<Set<T>>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<Set<T>> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        Set<T> actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertFalse(actual.isEmpty(), "Output atual deve conter pelo menos um elemento");
        actual.forEach(Assertions::assertNotNull);
    }

    default <T> void assertEmptySetResponse(ResponseEntity<ApiResponseWrapper<Set<T>>> response) {
        assertNotNull(response, "Response não deve ser null");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code deve ser OK");

        ApiResponseWrapper<Set<T>> body = response.getBody();
        assertNotNull(body, "Body do response não deve ser null");

        Set<T> actual = body.getBody();
        assertNotNull(actual, "Output atual não deve ser null");
        assertTrue(actual.isEmpty(), "Output atual deve estar vazio");
    }
}
