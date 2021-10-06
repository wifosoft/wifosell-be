package com.wifosell.zeus.controller;

import com.wifosell.zeus.annotation.PreAuthorizeAccessToShop;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.ShopService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
public class ShopController {
    @Autowired
    ShopService shopService;

    //Begin API

    /**
     * Lấy thông tin toàn bộ cửa hàng trong chuỗi
     *
     * @param userPrincipal
     * @return
     */

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getRelevantShop")
    public ResponseEntity<GApiResponse<List<Shop>>> getRelevantShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal) {
        List<Shop> shops = shopService.getRelevantShop(userPrincipal.getId());
        return new ResponseEntity<>(GApiResponse.success(shops), HttpStatus.OK);
    }



    /**
     * API xem thông tin cửa hàng
     *
     * @param userPrincipal
     * @param shopId
     * @return
     */
    @PreAuthorizeAccessToShop
    @GetMapping("/{shopId}")
    public ResponseEntity<GApiResponse> getShopInfo(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @PathVariable("shopId") Long shopId) {
        Shop shop = shopService.getShopInfo(shopId);
        return ResponseEntity.ok(GApiResponse.success(shop));
    }

    /**
     * [WFSLL-63] Thêm cửa hàng mới
     * @param userPrincipal
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse> addShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @RequestBody ShopRequest shopRequest){
        Shop shop = shopService.addShop(shopRequest, userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(shop));
    }

    //End API


    @GetMapping("/{id}/update")
    public ResponseEntity<GApiResponse> updateShopInfo(@CurrentUser UserPrincipal userPrincipal, @RequestParam("id") Long id) {
        return ResponseEntity.ok(GApiResponse.success("Update Shop Info by id: " + id.toString()));
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<GApiResponse> deleteShopInfo(@CurrentUser UserPrincipal userPrincipal, @RequestParam("id") Long id) {
        return ResponseEntity.ok(GApiResponse.success("Delete shop by id " + id.toString()));
    }

    /*
     *  Lấy danh sách shop có quyền quản lý
     */
    @GetMapping("/getCreatedShop")
    public ResponseEntity<GApiResponse> getCreatedShop(@CurrentUser UserPrincipal userPrincipal) {
        List<Shop> shops = shopService.getCreatedShop(userPrincipal.getId());
        return new ResponseEntity<>(GApiResponse.success(shops), HttpStatus.OK);
    }

    @ApiOperation(value = "givePermissionManageShop", notes = "Cấp quyền truy cập 1 cửa hàng cho 1 tài khoản")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lấy thông tin thành công", response = User.class),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")
    })
    /*
     * Cấp quyền quản lý shop trong chuỗi cho 1 tài khoản con
     * Chỉ quản lý mới được thêm shop cho 1 tài khoản trong chuỗi
     */
    @GetMapping("/givePermissionManageShop")
    public ResponseEntity<GApiResponse> givePermissionManageShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @RequestParam("userId") Long userId, @RequestParam("shopId") Long shopId) {
        if (userPrincipal.getParent() != null) {
            //Không cho phép truy cập nếu không phải là tổng quản lý
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PERMISSION_DENIED));
        }

        shopService.givePermissionManageShop(userId, shopId);
        return new ResponseEntity<>(GApiResponse.success("OK"), HttpStatus.OK);
    }


}
