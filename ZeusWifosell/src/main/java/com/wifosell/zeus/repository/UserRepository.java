package com.wifosell.zeus.repository;


import com.wifosell.zeus.exception.ResourceNotFoundException;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.security.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@ApiIgnore
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(@NotBlank String username);

    Optional<User> findByEmail(@NotBlank String email);

    Boolean existsByUsername(@NotBlank String username);

    Boolean existsByEmail(@NotBlank String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    default User getUser(UserPrincipal currentUser) {
        return getUserByName(currentUser.getUsername());
    }

    default User getUserByName(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
