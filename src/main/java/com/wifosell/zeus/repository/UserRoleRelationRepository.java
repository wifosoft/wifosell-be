package com.wifosell.zeus.repository;

import com.wifosell.zeus.model.role.UserRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRelationRepository extends JpaRepository<UserRoleRelation,  Long> {
}
