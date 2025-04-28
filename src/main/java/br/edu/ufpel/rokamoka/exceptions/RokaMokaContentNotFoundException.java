package br.edu.ufpel.rokamoka.exceptions;

/**
 * Exception thrown when attempting to access or modify a resource that does not exist.
 * <p>
 * For example, if a user tries to retrieve or update a record that doesn't exist in the database, this exception is
 * thrown.
 *
 * @author mauriciomucci
 */
public class RokaMokaContentNotFoundException extends Throwable {

    public RokaMokaContentNotFoundException(String message) {
        super(message);
    }
}
