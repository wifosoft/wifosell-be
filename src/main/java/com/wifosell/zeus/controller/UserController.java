package com.wifosell.zeus.controller;

import com.wifosell.zeus.annotation.PreAuthorizeAccessToUser;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.RegisterRequest;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.user.ChangePasswordRequest;
import com.wifosell.zeus.payload.request.user.ChangeRoleRequest;
import com.wifosell.zeus.payload.request.user.UpdateUserRequest;
import com.wifosell.zeus.payload.response.AvailableResourceResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Lấy thông tin cá nhân tài khoản dã đăng nhập
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<GApiResponse<User>> getMeInfo(@ApiIgnore @CurrentUser UserPrincipal currentUser) {
        User me = userService.getUserInfo(currentUser.getId());
        return ResponseEntity.ok(GApiResponse.success(me));
    }

    // Cập nhật thông tin cá nhân
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/update")
    public ResponseEntity<GApiResponse<User>> editMeInfo(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        User userChanged = userService.updateUserInfo(userPrincipal.getId(), updateUserRequest);
        return ResponseEntity.ok(GApiResponse.success(userChanged));
    }

    // Thay đổi mật khẩu cá nhân
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/changePassword")
    public ResponseEntity<GApiResponse<String>> changeMePassword(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userPrincipal.getId(), changePasswordRequest, true);
        return ResponseEntity.ok(GApiResponse.success("Change password successfully!"));
    }

    // Cập nhật thông tin của tài khoản nhân viên của mình
    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/update")
    public ResponseEntity<GApiResponse<User>> updateUserInfo(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        User userChanged = userService.updateUserInfo(userId, updateUserRequest);
        return ResponseEntity.ok(GApiResponse.success(userChanged));
    }

    // Reset mật khẩu tài khoản nhân viên
    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/resetPassword")
    public ResponseEntity<GApiResponse<String>> changeUserPassword(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userId, changePasswordRequest, false);
        return ResponseEntity.ok(GApiResponse.success("Change password successfully!"));
    }

    // Lấy thông tin tài khoản con
    @PreAuthorizeAccessToUser
    //@PreAuthorize("isAuthenticated() and @userService.hasAccessToUser(#userPrincipal, #id)")
    @GetMapping("/{id}")
    public ResponseEntity<GApiResponse<User>> getUserInfo(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId) {
        User user = userService.getUserInfo(userId);
        return ResponseEntity.ok(GApiResponse.success(user));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<User>>> deactivateUsers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<User> childAccounts = userService.getAllChildAccounts(userPrincipal);
        List<Long> filterListAccount = childAccounts.stream()
                .map(User::getId)
                .filter(id -> (request.getIds().contains(id)))
                .collect(Collectors.toList());
        List<User> affectedUser = userService.deactivateListUser(filterListAccount);
        return ResponseEntity.ok(GApiResponse.success(affectedUser));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<User>>> activateUsers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<User> childAccounts = userService.getAllChildAccounts(userPrincipal);
        List<Long> filterListAccount = childAccounts.stream()
                .map(User::getId)
                .filter(id -> (request.getIds().contains(id)))
                .collect(Collectors.toList());
        List<User> affectedUser = userService.activateListUser(filterListAccount);
        return ResponseEntity.ok(GApiResponse.success(affectedUser));
    }

    @PreAuthorizeAccessToUser
    @GetMapping("/{id}/deactivate")
    public ResponseEntity<GApiResponse<User>> deactivateUser(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId) {
        User user = userService.deactivateUser(userId);
        return ResponseEntity.ok(GApiResponse.success(user));
    }

    // Kích hoạt lại tài khoản theo soft delete
    @PreAuthorizeAccessToUser
    @GetMapping("/{id}/activate")
    public ResponseEntity<GApiResponse<User>> activateUser(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId) {
        User user = userService.activateUser(userId);
        return ResponseEntity.ok(GApiResponse.success(user));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addChildAccount")
    public ResponseEntity<GApiResponse<User>> addChildAccount(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody RegisterRequest registerRequest) {
        User childAccount = userService.addChildAccount(userPrincipal.getId(), registerRequest);

        return new ResponseEntity<>(GApiResponse.success(childAccount), HttpStatus.OK);
    }

    // Lấy danh sách tài khoản con (cần là tài khoản GENERAL_MANAGER)
    @PreAuthorize("hasRole('GENERAL_MANAGER')")
    @GetMapping("/listChildAccount")
    public ResponseEntity<GApiResponse<List<User>>> getListChildAccounts(@CurrentUser UserPrincipal userPrincipal) {
        List<User> childAccounts = userService.getAllChildAccounts(userPrincipal);
        return ResponseEntity.ok(GApiResponse.success(childAccounts));
    }

    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/changeRoleAccount")
    public ResponseEntity<GApiResponse<User>> changeRoleAccount(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @RequestBody @Valid ChangeRoleRequest changeRoleRequest) {
        User user = userService.changeRole(userId, changeRoleRequest.getListRoleString());
        return ResponseEntity.ok(GApiResponse.success(user));
    }

    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/changePermissionAccount")
    public ResponseEntity<GApiResponse<User>> changePermissionAccount(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @RequestBody @Valid ChangeRoleRequest changeRoleRequest) {
        User user = userService.changePermission(userId, changeRoleRequest.getListRoleString());
        return ResponseEntity.ok(GApiResponse.success(user));
    }

    //TODO Refractor and move to shop entrypoint
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getShopManage")
    public ResponseEntity<GApiResponse<List<Shop>>> getShopManage(@CurrentUser UserPrincipal userPrincipal) {
        List<Shop> listShopManage = userService.getListShopManage(userPrincipal);
        return new ResponseEntity<>(GApiResponse.success(listShopManage), HttpStatus.OK);
    }

    @GetMapping("/checkUsernameAvailable")
    public ResponseEntity<GApiResponse<AvailableResourceResponse>> checkUsernameExist(@RequestParam(value = "username") String username) {
        AvailableResourceResponse resourceResponse = userService.checkUsernameAvailable(username);
        return new ResponseEntity<>(GApiResponse.success(resourceResponse), HttpStatus.OK);
    }

    @GetMapping("/checkEmailAvailable")
    public ResponseEntity<GApiResponse<AvailableResourceResponse>> checkEmailExist(@RequestParam(value = "email") String email) {
        AvailableResourceResponse resourceResponse = userService.checkEmailAvailable(email);
        return new ResponseEntity<>(GApiResponse.success(resourceResponse), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User newUser = userService.addUser(user);
        return new ResponseEntity<User>(newUser, HttpStatus.OK);
    }
}
