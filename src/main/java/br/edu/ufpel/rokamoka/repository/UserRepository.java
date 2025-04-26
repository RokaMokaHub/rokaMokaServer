package br.edu.ufpel.rokamoka.repository;

import br.edu.ufpel.rokamoka.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A JPA repository for User objects.
 *
 * <p>This repository provides methods for creating, reading, updating and deleting
 * User objects in the database.
 *
 * @author iyisakuma
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves an Optional containing a User with the specified name.
     *
     * @param nome The name of the user to retrieve.
     * @return An Optional containing the found User, or an empty Optional if no user is found with the given name.
     */
    Optional<User> findByNome(String nome);

    /**
     * Verifies if a User with the given email already exists in the database.
     *
     * @param email The email address to check.
     * @return true if a user with the given email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Verifies if a User with the given name already exists in the database.
     *
     * @param nome The name of the user to check.
     * @return true if a user with the given name exists, false otherwise.
     */
    boolean existsByNome(String nome);

    /**
     * Verifies if a User with the given name and email already exists in the database.
     *
     * @param nome The name of the user to check.
     * @param email The email of the user to check.
     * @return {@code true} if a user with the given name and email exists, {@code false} otherwise.
     */
    boolean existsByNomeAndEmail(String nome, String email);
}
