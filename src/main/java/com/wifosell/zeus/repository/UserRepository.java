package com.wifosell.zeus.repository;


import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.security.UserPrincipal;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@ApiIgnore
@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
public interface UserRepository extends SoftDeleteCrudRepository<User, Long> {


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
                .orElseThrow(
                        () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_FOUND))
                );
    }

    default User getUserById(Long id) {
        return findById(id).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_FOUND))
        );
    }
}
