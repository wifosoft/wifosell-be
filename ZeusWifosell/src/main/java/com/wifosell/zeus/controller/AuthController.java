package com.wifosell.zeus.controller;

import com.wifosell.zeus.constant.DefaultUserPermission;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.LoginRequest;
import com.wifosell.zeus.payload.request.RegisterRequest;
import com.wifosell.zeus.payload.response.LoginResponse;
import com.wifosell.zeus.repository.RoleRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String USER_ROLE_NOT_SET = "User role not set";


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<GApiResponse<LoginResponse>>authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(GApiResponse.success(new LoginResponse(jwt)));
    }

    @PostMapping("/register")
    public ResponseEntity<GApiResponse<User>> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
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

        User user = new User(firstName, lastName, username, email, password);

        List<Role> roles = new ArrayList<>();
        if (username.equals("admin")) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
            roles.add(roleRepository.findByName(RoleName.ROLE_GENERAL_MANAGER)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_GENERAL_MANAGER)
                    .orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
        }
        user.setRoles(roles);
        user.setUserPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER));
        User result = userRepository.save(user);

        return ResponseEntity.ok(new GApiResponse<User>(Boolean.TRUE, "User registered successfully", result));

    }

}
