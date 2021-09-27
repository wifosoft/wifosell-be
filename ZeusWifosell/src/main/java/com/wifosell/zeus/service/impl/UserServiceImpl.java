package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.exception.ResourceNotFoundException;
import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.response.AvailableResourceResponse;
import com.wifosell.zeus.payload.response.GetMeInfoResponse;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.UserService;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.swing.text.StyledEditorKit;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ShopRepository shopRepository;


    /**
     * Return the information object of user who request to the server
     *
     * @param currentUser Current User from Authentication
     * @return The information of current use
     */
    @Override
    public GetMeInfoResponse getMeInfo(UserPrincipal currentUser) {
        return GetMeInfoResponse.builder()
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .userName(currentUser.getUsername())
                .phone(currentUser.getPhone())
                .email(currentUser.getEmail())
                .authorities(currentUser.getHumanAuthorities())
                .parent(currentUser.getParent())
                .build();
    }

    @Override
    public boolean hasAccessToShop(UserPrincipal currentUser, Long userId) {
        return false;
    }

    @Override
    public boolean hasAccessToUser(UserPrincipal currentUser, Long shopId) {
        return false;
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
        permissions.add(UserPermission.GLOBAL_ACCESS);
        permissions.add(UserPermission.ADMIN_ACCESS);
        user.setUserPermission(permissions);
        return userRepository.save(user);
    }

}
