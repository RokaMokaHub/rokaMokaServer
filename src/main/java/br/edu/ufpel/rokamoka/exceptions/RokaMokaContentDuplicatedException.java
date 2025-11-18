package br.edu.ufpel.rokamoka.exceptions;

/**
 * Exception thrown when a user tries to create a resource that already exists.
 * <p>
 * For example, if a user tries to create a user with an email that already exists in the database, this exception is
 * thrown.
 *
 * @author iyiSakuma
 */
public class RokaMokaContentDuplicatedException extends RuntimeException {

    public RokaMokaContentDuplicatedException(String s) {
        super(s);
    }
}
