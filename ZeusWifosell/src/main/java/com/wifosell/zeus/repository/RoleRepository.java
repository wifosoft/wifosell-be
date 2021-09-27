package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@ApiIgnore
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
