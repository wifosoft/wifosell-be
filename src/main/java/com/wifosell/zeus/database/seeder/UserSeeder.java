package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.constant.DefaultUserPermission;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
public class UserSeeder extends BaseSeeder implements ISeeder {

    RoleRepository roleRepository;


    public static PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void prepareJpaRepository() {
        roleRepository = this.factory.getRepository(RoleRepository.class);
    }

    @Override
    public void run() {
        String password = encoder().encode("admin123");
        User adminUser = User.builder()
                .username("admin")
                .password(password)
                .email("admin@wifosoft.com")
                .firstName("Duc Minh")
                .lastName("Tran")
                .address("Đường Lê Thánh Tông, quận 1, thành phố Đà Nẵng")
                .userPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER))
                .phone("0982259245")
                .build();

        User manager1 = User.builder()
                .username("manager1")
                .password(password)
                .email("manager1@wifosoft.com")
                .firstName("Nguyễn Văn")
                .lastName("An 1")
                .address("Đường Nguyễn Văn Cừ, quận 5, thành phố Hồ Chí Minh")
                .phone("0982259246")
                .parent(null)
                .userPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER))
                .build();

        User manager2 = User.builder()
                .username("manager2")
                .password(password)
                .email("manager2@wifosoft.com")
                .firstName("Nguyễn Văn")
                .lastName("Thứ 2")
                .address("Đường Trần Hưng Đạo, Quận Tân Phú, Thành Phố Hồ Chí Min")
                .phone("0982259247")
                .parent(null)
                .userPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER))
                .build();


        List<User> users = new ArrayList<>();
        users.add(adminUser);
        users.add(manager1);
        users.add(manager2);
        Role gmRole = roleRepository.getRoleByName(RoleName.ROLE_GENERAL_MANAGER);
        Role adminRole = roleRepository.getRoleByName(RoleName.ROLE_ADMIN);
        users.forEach(e -> {
            entityManager.persist(e);
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUser(e);
            userRoleRelation.setRole(gmRole);
            if (e.getUsername().equals("admin")) {
                UserRoleRelation userRoleRelationAdmin = new UserRoleRelation();
                userRoleRelationAdmin.setUser(e);
                userRoleRelationAdmin.setRole(adminRole);
                entityManager.persist(userRoleRelationAdmin);
            }
            entityManager.persist(userRoleRelation);
        });


    }
}
