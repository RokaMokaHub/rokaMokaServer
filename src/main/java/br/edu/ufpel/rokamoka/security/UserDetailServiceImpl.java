package br.edu.ufpel.rokamoka.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.ufpel.rokamoka.repository.UserRepository;

import lombok.AllArgsConstructor;

/**
 * A service for loading user details by username.
 *
 * <p>This service is used by Spring Security to load user details by username.
 * It retrieves the user's details from the repository using their username.
 *
 * @author iyisakuma
 */
@AllArgsConstructor
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads the user details by username.
     *
     * <p>This method retrieves a user's details from the repository using their username.
     * If a user is found, it maps the user to a {@link UserAuthenticated} object.
     * If no user is found, it throws a {@link UsernameNotFoundException}.
     *
     * @param username the username of the user to load
     * @return a {@link UserDetails} object containing the user's information
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByNome(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
