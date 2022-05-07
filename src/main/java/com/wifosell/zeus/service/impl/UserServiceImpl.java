package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.exception.ResourceNotFoundException;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.RegisterRequest;
import com.wifosell.zeus.payload.request.user.ChangePasswordRequest;
import com.wifosell.zeus.payload.request.user.UpdateUserRequest;
import com.wifosell.zeus.payload.response.AvailableResourceResponse;
import com.wifosell.zeus.repository.RoleRepository;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.UserRoleRelationRepository;
import com.wifosell.zeus.security.SecurityCheck;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.ShopService;
import com.wifosell.zeus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.*;

@Transactional(rollbackFor = ZeusGlobalException.class)
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private TransactionTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ShopService shopService;


    @Autowired
    private UserRoleRelationRepository userRoleRelationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private SecurityCheck securityCheck;


    //    @PreAuthorize("@securityCheck.check(authentication, #id)")
    @Override
    public User getUserInfo(Long userId) {
        User user = userRepository.getUserById(userId);
        return user;
    }

    @Override
    public User updateUserInfo(Long userId, @Valid UpdateUserRequest updateUserRequest) {
        User user = userRepository.getUserById(userId);
        Optional.ofNullable(updateUserRequest.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(updateUserRequest.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(updateUserRequest.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(updateUserRequest.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(updateUserRequest.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(updateUserRequest.getIsActive()).ifPresent(user::setIsActive);
        userRepository.save(user);


//        if(user.getId() == 2){
//            throw new AppException("Exception");
//        }
//        user.setAddress("Cap nhat");
//        //userRepository.save(user);
//        userRepository.save(user);

        return user;
    }


    @Override
    public void changePassword(Long userId, @Valid ChangePasswordRequest changePasswordRequest, boolean flagOld) {
        User user = userRepository.getUserById(userId);

        if (flagOld) {
            if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_OLD_PASSWORD_INCORRECT));
            }
        }

        String newPassword = changePasswordRequest.getNewPassword();
        String verifyPassword = changePasswordRequest.getVerifyPassword();
        if (!newPassword.equals(verifyPassword)) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_VERIFIED_PASSWORD_INCORRECT));
        }

        String hashPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(hashPassword);
        userRepository.save(user);
    }


    @Override
    public User deActivateUser(Long userId) {
        User user = userRepository.getUserById(userId);
        if (user.isActive()) {
            userRepository.delete(user);
        }
        return user;
    }

    @Override
    public User activateUser(Long userId) {
        User user = userRepository.getUserById(userId);
        if (!user.isActive()) {
            userRepository.recover(user);
        }
        return user;
    }

    @Override
    public List<User> deActivateListUser(List<Long> userList) {
        List<User> affectedUser = new ArrayList<>();
        for (Long user : userList) {
            affectedUser.add(this.deActivateUser(user));
        }
        return affectedUser;
    }

    @Override
    public List<User> activateListUser(List<Long> userList) {
        List<User> affectedUser = new ArrayList<>();
        for (Long user : userList) {
            affectedUser.add(this.activateUser(user));
        }
        return affectedUser;
    }

    @Override
    public User changeRole(Long userId, List<String> roles) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("Chao ban"));

        Set<UserRoleRelation> currentUserRoleRelation = user.getUserRoleRelation();
        for (UserRoleRelation roleRelation : currentUserRoleRelation) {
            Role role = roleRelation.getRole();
            if (!roles.contains(role.getName().name())) {
                //if role isn't existed, remove it from the db
                userRoleRelationRepository.delete(roleRelation);
            }
            roles.remove(role.getName().name());
        }
        List<Role> listRole = new ArrayList<>();
        for (String r : roles) {
            RoleName roleName = RoleName.valueOf(r);
            UserRoleRelation newRoleRelation = new UserRoleRelation();
            newRoleRelation.setRole(roleRepository.getRoleByName(roleName));
            newRoleRelation.setUser(user);
            userRoleRelationRepository.save(newRoleRelation);
        }
        return user;
    }

    @Override
    public User changePermission(Long userId, List<String> permissions) {
        User user = userRepository.getUserById(userId);
        List<UserPermission> lsUserPermission = new ArrayList<>();
        for (String permission : permissions) {
            try {
                UserPermission userPermission = UserPermission.valueOf(permission);
                lsUserPermission.add(userPermission);
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PERMISSION_NOT_FOUND));
            }
        }
        lsUserPermission = Collections.unmodifiableList(lsUserPermission);
        user.setUserPermission(lsUserPermission);
        userRepository.save(user);
        return user;
    }

    @Modifying
    @Override
    public User addChildAccount(Long parentId, RegisterRequest registerRequest) {
        //entityManager.persist(user);
        // entityManager.getTransaction().commit();
        User parent = userRepository.getUserById(parentId);
        if (Boolean.TRUE.equals(userRepository.existsByUsername(registerRequest.getUsername()))) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USERNAME_HAS_BEEN_TAKEN, "Username has been taken"));
        }
        if (Boolean.TRUE.equals(userRepository.existsByEmail(registerRequest.getEmail()))) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.EMAIL_HAS_BEEN_TAKEN, "Email has been taken"));
        }
        String firstName = registerRequest.getFirstName().toLowerCase();

        String lastName = registerRequest.getLastName().toLowerCase();

        String username = registerRequest.getUsername().toLowerCase();

        String email = registerRequest.getEmail().toLowerCase();

        String phone = registerRequest.getPhone().toLowerCase();

        String password = passwordEncoder.encode(registerRequest.getPassword());

        String address = registerRequest.getAddress();
        User user1 = new User(firstName, lastName, username, email, phone, password);
        user1.setAddress(address);
        user1.setParent(parent);
        userRepository.save(user1);
        return user1;
    }

    /**
     * Kiểm tra quyền truy cập : là tài khoản GeneralManager của shopId  (tạo ra shopId)
     *
     * @param currentUser
     * @param shopId
     * @return
     */
    @Override
    public boolean hasAccessGeneralManagerToShop(UserPrincipal currentUser, Long shopId) {
        List<Shop> lsShopCanAccess = shopService.getCreatedShop(currentUser.getId());
        return lsShopCanAccess.stream().anyMatch(x -> x.getId().equals(shopId));
    }

    /**
     * Kiểm tra có quyền truy cập vào shop X
     *
     * @param currentUser
     * @param shopId
     * @return
     */
    @Override
    public boolean hasAccessToShop(UserPrincipal currentUser, Long shopId) {
        List<Shop> lsShopCanAccess = shopService.getCanAccessShop(currentUser.getId());
        return lsShopCanAccess.stream().anyMatch(x -> x.getId().equals(shopId));
    }

    /**
     * Kiểm tra có quyền truy cập vào danh sách shop của tài khoản cha
     *
     * @param currentUser
     * @param shopId
     * @return
     */
    @Override
    public boolean hasAccessToRelevantShop(UserPrincipal currentUser, Long shopId) {
        List<Shop> lsShopCanAccess = shopService.getRelevantShop(currentUser.getId());
        return lsShopCanAccess.stream().anyMatch(x -> x.getId().equals(shopId));
    }

    /**
     * Check current user have permission with userId
     *
     * @param currentUser
     * @param userId
     * @return
     */
    @Override
    public boolean hasAccessToUser(UserPrincipal currentUser, Long userId) {
        List<User> childs = userRepository.findById(currentUser.getId()).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_FOUND))
        ).getChildrenUsers();
        return childs.stream().anyMatch(x -> x.getId().equals(userId));
    }

    @Override
    public List<User> getAllChildAccounts(UserPrincipal currentUser) {
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId())).getChildrenUsers();
    }

    @Override
    public List<Shop> getListShopManage(UserPrincipal userPrincipal) {
        User currentUser = userRepository.getUser(userPrincipal);
        return currentUser.getAccessShops();
    }

    @Override
    public AvailableResourceResponse checkEmailAvailable(String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new AvailableResourceResponse(isAvailable);
    }

    @Override
    public AvailableResourceResponse checkUsernameAvailable(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new AvailableResourceResponse(isAvailable);
    }


    @Override
    public User addUser(User user) {
        List<UserPermission> permissions = new ArrayList<>();
//        permissions.add(UserPermission.GLOBAL_ACCESS);
//        permissions.add(UserPermission.ADMIN_ACCESS);
        user.setUserPermission(permissions);
        return userRepository.save(user);
    }

}
