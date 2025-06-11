package br.edu.ufpel.rokamoka.dto.user.output;

/**
 * A Data Transfer Object for a user's response after a successful authentication.
 *
 * @author iyisakuma
 */
public record UserAuthDTO(String jwt) {

}
