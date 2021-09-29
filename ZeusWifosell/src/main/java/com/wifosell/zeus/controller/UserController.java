package com.wifosell.zeus.controller;

import com.wifosell.zeus.annotation.PreAuthorizeAccessToUser;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiResponse;
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

    @GetMapping("/me")
    public ResponseEntity<GApiResponse<User>> getMeInfo(@ApiIgnore @CurrentUser UserPrincipal currentUser) {
        User me = userService.getUserInfo(currentUser.getId());
        return ResponseEntity.ok(GApiResponse.success(me));
    }

    @PreAuthorizeAccessToUser
    //@PreAuthorize("isAuthenticated() and @userService.hasAccessToUser(#userPrincipal, #id)")
    @GetMapping("/{id}")
    public ResponseEntity<GApiResponse<User>> getUserInfo(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long id) {
        User user = userService.getUserInfo(id);
        return ResponseEntity.ok(GApiResponse.success(user));
    }

    /**
     * Lấy danh sách tài khoản con mà Root admin tạo
     * P: Cần là tài khoản GENERAL_MANAGER
     *
     * @param currentUser
     * @return
     */
    @PreAuthorize("hasRole('GENERAL_MANAGER')")
    @GetMapping("/childAccount")
    public ResponseEntity<GApiResponse<List<User>>> getListChildAccounts(@CurrentUser UserPrincipal currentUser) {
        GApiResponse<List<User>> child_accounts = userService.getAllChildAccounts(currentUser);
        return new ResponseEntity<>(child_accounts, HttpStatus.OK);
    }


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
