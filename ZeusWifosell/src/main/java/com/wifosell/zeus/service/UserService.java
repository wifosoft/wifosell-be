package com.wifosell.zeus.service;

import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.response.AvailableResourceResponse;
import com.wifosell.zeus.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    User getUserInfo(Long id);

    boolean hasAccessToShop(UserPrincipal currentUser, Long userId);
    boolean hasAccessToUser( UserPrincipal currentUser, Long shopId);
    GApiResponse<List<User>> getAllChildAccounts(UserPrincipal currentUser);
    List<Shop> getListShopManage(UserPrincipal userPrincipal);

    AvailableResourceResponse checkEmailAvailable(String email);
    AvailableResourceResponse checkUsernameAvailable(String username);

    User addUser(User user);

}
