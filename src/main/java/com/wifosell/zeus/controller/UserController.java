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
import com.wifosell.zeus.security.SecurityCheck;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {
    @Autowired
    private UserService userService;


    @Autowired
    private SecurityCheck securityCheck;

    /**
     * Lấy thông tin tài khoản đã đăng nhập bằng Token truyền lên=
     *
     * @param currentUser
     * @return Thông tin tài khoản
     */
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "getMeInfo", notes = "Lấy thông tin cá nhân")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lấy thông tin thành công", response = User.class),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")
    })

    /**
     * Lấy thông tin cá nhân tài khoản dã đăng nhập
     */
    @GetMapping("/me")
    public ResponseEntity<GApiResponse<User>> getMeInfo(@ApiIgnore @CurrentUser UserPrincipal currentUser) {
        User me = userService.getUserInfo(currentUser.getId());
        return ResponseEntity.ok(GApiResponse.success(me));
    }

    /**
     * Cập nhật thông tin cá nhân
     *
     * @param userPrincipal
     * @param updateUserRequest
     * @return Thông tin tài khoản đã chỉnh sửa
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/update")
    public ResponseEntity<GApiResponse> editMeInfo(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        User userChanged = userService.updateUserInfo(userPrincipal.getId(), updateUserRequest);
        return new ResponseEntity(GApiResponse.success(userChanged), HttpStatus.OK);
    }

    /**
     * Thay đổi mật khẩu cá nhân
     *
     * @param userPrincipal
     * @param changePasswordRequest
     * @return Thay đổi mật khẩu thnàh công
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me/changePassword")
    public ResponseEntity<GApiResponse> changeMePassword(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userPrincipal.getId(), changePasswordRequest, true);
        return new ResponseEntity(GApiResponse.success("Change password successfully!"), HttpStatus.OK);
    }


    /**
     * Cập nhật thông tin của tài khoản nhân viên của mình
     *
     * @param userPrincipal
     * @param userId
     * @param updateUserRequest
     * @return Thông tin tài khoản đã chỉnh sửa
     */
    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/update")
    public ResponseEntity<GApiResponse> updateUserInfo(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        User userChanged = userService.updateUserInfo(userId, updateUserRequest);
        return new ResponseEntity<>(GApiResponse.success(userChanged), HttpStatus.OK);
    }

    /***
     * Reset mật khẩu tài khoản nhân viên
     * @param userPrincipal
     * @param userId
     * @param changePasswordRequest
     * @return Thành công
     */
    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/resetPassword")
    public ResponseEntity<GApiResponse> changeUserPassword(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(userId, changePasswordRequest, false);
        return new ResponseEntity(GApiResponse.success("Change password successfully!"), HttpStatus.OK);
    }

    /**
     * Lấy thông tin tài khoản con
     *
     * @param userPrincipal
     * @param userId
     * @return
     */
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
            @RequestBody ListIdRequest request
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
            @RequestBody ListIdRequest request
    ) {
        List<User> childAccounts = userService.getAllChildAccounts(userPrincipal);
        List<Long> filterListAccount = childAccounts.stream()
                .map(User::getId)
                .filter(id -> (request.getIds().contains(id)))
                .collect(Collectors.toList());
        List<User> affectedUser = userService.activateListUser(filterListAccount);
        return ResponseEntity.ok(GApiResponse.success(affectedUser));
    }


    /**
     * Ngừng hoạt động tài khoản theo soft delete
     *
     * @param userPrincipal
     * @param userId
     * @return
     */
    @PreAuthorizeAccessToUser
    @GetMapping("/{id}/deactivate")
    public ResponseEntity<GApiResponse<User>> deActiveUser(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId) {
        User user = userService.deactivateUser(userId);
        return ResponseEntity.ok(GApiResponse.success(user));
    }


    /**
     * Kích hoạt lại tài khoản theo soft delete
     *
     * @param userPrincipal
     * @param userId
     * @return
     */
    @PreAuthorizeAccessToUser
    @GetMapping("/{id}/activate")
    public ResponseEntity<GApiResponse<User>> activeUser(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId) {
        User user = userService.activateUser(userId);
        return ResponseEntity.ok(GApiResponse.success(user));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addChildAccount")
    public ResponseEntity<GApiResponse<User>> addChildAccount(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody RegisterRequest registerRequest) {
        User childAccount = userService.addChildAccount(userPrincipal.getId(), registerRequest);

        return new ResponseEntity<>(GApiResponse.success(childAccount), HttpStatus.OK);
    }


    /**
     * Lấy danh sách tài khoản con
     * P: Cần là tài khoản GENERAL_MANAGER
     *
     * @return
     */
    @PreAuthorize("hasRole('GENERAL_MANAGER')")
    @GetMapping("/listChildAccount")
    public ResponseEntity<GApiResponse<List<User>>> getListChildAccounts(@CurrentUser UserPrincipal userPrincipal) {
        List<User> childAccounts = userService.getAllChildAccounts(userPrincipal);
        return ResponseEntity.ok(GApiResponse.success(childAccounts));
    }

    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/changeRoleAccount")
    public ResponseEntity<GApiResponse> changeRoleAccount(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @RequestBody ChangeRoleRequest changeRoleRequest) {
        User user = userService.changeRole(userId, changeRoleRequest.getListRoleString());
        return new ResponseEntity<>(GApiResponse.success(user), HttpStatus.OK);
    }

    @PreAuthorizeAccessToUser
    @PostMapping("/{id}/changePermissionAccount")
    public ResponseEntity<GApiResponse> changePermissionAccount(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long userId, @RequestBody ChangeRoleRequest changeRoleRequest) {
        User user = userService.changePermission(userId, changeRoleRequest.getListRoleString());
        return new ResponseEntity<>(GApiResponse.success(user), HttpStatus.OK);
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
