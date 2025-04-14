package br.edu.ufpel.rokamoka.exceptions;

/**
 * Exception thrown when a user tries to create a resource that already exists.
 * <p>
 * For example, if a user tries to create a user with an email that already exists
 * in the database, this exception is thrown.
 *
 * @author iyiSakuma
 * @since 0.1.0
 */
public class RokaMokaContentDuplicatedException extends Throwable {

    public RokaMokaContentDuplicatedException(String s) {
        super(s);
    }
}
