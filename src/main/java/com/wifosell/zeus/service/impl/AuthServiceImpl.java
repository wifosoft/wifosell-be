package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.DefaultUserPermission;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.RegisterRequest;
import com.wifosell.zeus.repository.RoleRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.UserRoleRelationRepository;
import com.wifosell.zeus.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRelationRepository userRoleRelationRepository;

    @Autowired
    RoleRepository roleRepository;

    @Transactional
    @Override
    public User register(RegisterRequest registerRequest)  {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerRequest.getUserName()))) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USERNAME_HAS_BEEN_TAKEN, "Username has been taken"));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(registerRequest.getEmail()))) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.EMAIL_HAS_BEEN_TAKEN, "Email has been taken"));
        }


        String firstName = registerRequest.getFirstName().toLowerCase();

        String lastName = registerRequest.getLastName().toLowerCase();

        String username = registerRequest.getUserName().toLowerCase();

        String email = registerRequest.getEmail().toLowerCase();

        String password = passwordEncoder.encode(registerRequest.getPassword());

        String address = registerRequest.getAddress();
        User user = new User(firstName, lastName, username, email, password);
        user.setAddress(address);

        List<Role> roles = new ArrayList<>();
        if (username.equals("admin")) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.ROLE_NOT_FOUND))));
            roles.add(roleRepository.findByName(RoleName.ROLE_GENERAL_MANAGER)
                    .orElseThrow(() -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.ROLE_NOT_FOUND))));
        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_GENERAL_MANAGER)
                    .orElseThrow(() -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.ROLE_NOT_FOUND))));
        }
        Set<UserPermission> lsUserPermission;
        user.setUserPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER));
        User result = userRepository.save(user);
        if (username.equals("shop1")) {
            //throw new AppException("App exception");
            throw new AppException("System Application exception");
        }
        for (Role r : roles) {
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUser(user);
            userRoleRelation.setRole(r); //user.setRoles(roles);
            userRoleRelationRepository.save(userRoleRelation);

        }
        return result;
    }
}
