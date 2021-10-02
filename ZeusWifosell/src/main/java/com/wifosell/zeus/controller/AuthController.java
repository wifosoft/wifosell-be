package com.wifosell.zeus.controller;

import com.wifosell.zeus.constant.DefaultUserPermission;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.LoginRequest;
import com.wifosell.zeus.payload.request.RegisterRequest;
import com.wifosell.zeus.payload.response.LoginResponse;
import com.wifosell.zeus.repository.RoleRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.UserRoleRelationRepository;
import com.wifosell.zeus.security.JWTTokenProvider;
import com.wifosell.zeus.service.AuthService;
import com.wifosell.zeus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthController {
    private static final String USER_ROLE_NOT_SET = "User role not set";
    @Autowired
    AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRelationRepository userRoleRelationRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<GApiResponse<LoginResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(GApiResponse.success(new LoginResponse(jwt)));
    }

    @PostMapping("/register")
    public ResponseEntity<GApiResponse<User>> registerUser(@RequestBody @Valid RegisterRequest registerRequest)  {
        User result = authService.register(registerRequest);
        return ResponseEntity.ok(new GApiResponse<User>(Boolean.TRUE, "User registered successfully", result));

    }

}
