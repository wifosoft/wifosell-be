package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.exception.ResourceNotFoundException;
import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.response.AvailableResourceResponse;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.security.SecurityCheck;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private SecurityCheck securityCheck;


//    @PreAuthorize("@securityCheck.check(authentication, #id)")
    @Override
    public User getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () ->  new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_FOUND))
        );
        return user;
    }


    @Override
    public boolean hasAccessToShop(UserPrincipal currentUser, Long userId) {
        return false;
    }

    /**
     * Check current user have permission with userId
     * @param currentUser
     * @param userId
     * @return
     */
    @Override
    public boolean hasAccessToUser(UserPrincipal currentUser, Long userId)  {

        List<User> childs =  userRepository.findById(currentUser.getId()).orElseThrow(
                () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_FOUND))
        ).getChildrenUsers();
        return childs.stream().anyMatch(x -> x.getId().equals(userId));
    }

    @Override
    public GApiResponse<List<User>> getAllChildAccounts(UserPrincipal currentUser) {
        List<User> listchilds = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId())).getChildrenUsers();
        listchilds.forEach( e->  e.setParent(null));//remove parent field
        return new GApiResponse(Boolean.TRUE, "" ,  listchilds);
    }

    @Override
    public List<Shop> getListShopManage(UserPrincipal userPrincipal) {
        User currentUser = userRepository.getUser(userPrincipal);
        return currentUser.getShops();
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
