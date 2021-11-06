package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.Seeder;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class RoleSeeder extends BaseSeeder implements ISeeder {
    RoleRepository roleRepository;

    @Override
    public void prepareJpaRepository() {
        roleRepository = factory.getRepository(RoleRepository.class);
    }

    @Override
    public void run() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_ADMIN));
        roles.add(new Role(RoleName.ROLE_GENERAL_MANAGER));
        roles.add(new Role(RoleName.ROLE_SALE_STAFF));
        roles.add(new Role(RoleName.ROLE_ACCOUNTANT_STAFF));
        roles.add(new Role(RoleName.ROLE_WAREHOUSE_STAFF));
        roleRepository.saveAll(roles);
    }
}
