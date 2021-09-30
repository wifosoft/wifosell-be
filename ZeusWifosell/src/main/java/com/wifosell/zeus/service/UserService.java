package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.user.ChangePasswordRequest;
import com.wifosell.zeus.payload.request.user.UpdateUserRequest;
import com.wifosell.zeus.payload.response.AvailableResourceResponse;
import com.wifosell.zeus.security.UserPrincipal;
import org.apache.coyote.Request;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    User getUserInfo(Long userId);

    User updateUserInfo(Long userId, UpdateUserRequest updateUserRequest);
    void changePassword(Long userId, ChangePasswordRequest changePasswordRequest, boolean flagOld);

    User deActiveUser(Long userId);
    User activeUser(Long userId);

    boolean hasAccessToShop(UserPrincipal currentUser, Long userId);
    boolean hasAccessToUser( UserPrincipal currentUser, Long shopId);

    GApiResponse<List<User>> getAllChildAccounts(UserPrincipal currentUser);

    List<Shop> getListShopManage(UserPrincipal userPrincipal);

    AvailableResourceResponse checkEmailAvailable(String email);
    AvailableResourceResponse checkUsernameAvailable(String username);

    User addUser(User user);

}
