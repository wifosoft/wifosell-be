package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.RegisterRequest;
import com.wifosell.zeus.payload.request.user.ChangePasswordRequest;
import com.wifosell.zeus.payload.request.user.UpdateUserRequest;
import com.wifosell.zeus.payload.response.AvailableResourceResponse;
import com.wifosell.zeus.security.UserPrincipal;

import java.util.List;

public interface UserService {
    User getUserInfo(Long userId);

    User updateUserInfo(Long userId, UpdateUserRequest updateUserRequest);
    void changePassword(Long userId, ChangePasswordRequest changePasswordRequest, boolean flagOld);

    User deActivateUser(Long userId);
    User activateUser(Long userId);

    List<User> deActivateListUser(List<Long> userList);
    List<User> activateListUser(List<Long> userList);


    User changeRole(Long userId, List<String> roles);
    User changePermission(Long userId, List<String> permission);

    User addChildAccount(Long parentId, RegisterRequest registerRequest);

    boolean hasAccessGeneralManagerToShop(UserPrincipal currentUser, Long shopId);
    boolean hasAccessToShop(UserPrincipal currentUser, Long shopId);
    boolean hasAccessToRelevantShop(UserPrincipal currentUser, Long shopId);
    boolean hasAccessToUser( UserPrincipal currentUser, Long userId);

   List<User> getAllChildAccounts(UserPrincipal currentUser);

    List<Shop> getListShopManage(UserPrincipal userPrincipal);

    AvailableResourceResponse checkEmailAvailable(String email);
    AvailableResourceResponse checkUsernameAvailable(String username);

    User addUser(User user);

}
