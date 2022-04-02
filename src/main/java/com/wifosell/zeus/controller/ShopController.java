package com.wifosell.zeus.controller;

import com.wifosell.zeus.annotation.PreAuthorizeAccessGeneralManagerToShop;
import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.shop.ShopRequest;
import com.wifosell.zeus.payload.response.shop.ShopResponse;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shops")
public class ShopController {
    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    //Begin API

    /**
     * Lấy thông tin toàn bộ cửa hàng trong chuỗi
     *
     * @param userPrincipal
     * @return
     */

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getRelevantShop")
    public ResponseEntity<GApiResponse<List<ShopResponse>>> getRelevantShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal) {
        List<Shop> shops = shopService.getRelevantShop(userPrincipal.getId());
        List<ShopResponse> responses = shops.stream().map(ShopResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }


    /**
     * API xem thông tin cửa hàng
     *
     * @param userPrincipal
     * @param shopId
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{shopId}")
    public ResponseEntity<GApiResponse<ShopResponse>> getShopInfo(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @PathVariable("shopId") Long shopId) {
        Shop shop = shopService.getShopInfo(shopId);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    /**
     * [WFSLL-63] Thêm cửa hàng mới
     *
     * @param userPrincipal
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<ShopResponse>> addShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @RequestBody ShopRequest shopRequest) {
        if (userPrincipal.getParent() != null) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.SHOP_ADD_PERMISSION_BY_CHILD_USER));
        }
        Shop shop = shopService.addShop(shopRequest, userPrincipal.getId());
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    //[WFSLL-61]

    /**
     * [WFSLL-61] Cập nhật thông tin shop
     * Chỉ tài khoản GM mới access vào route này.
     *
     * @param userPrincipal
     * @param shopId
     * @param shopRequest
     * @return
     */
    @PreAuthorizeAccessGeneralManagerToShop
    @PostMapping("/{shopId}/update")
    public ResponseEntity<GApiResponse<ShopResponse>> editShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "shopId") Long shopId, @RequestBody ShopRequest shopRequest) {
        Shop shop = shopService.editShop(shopId, shopRequest);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    /**
     * Ngừng kinh doanh 1 shop
     *
     * @param userPrincipal
     * @param shopId
     * @return
     */
    @PreAuthorizeAccessGeneralManagerToShop
    @GetMapping("/{shopId}/deactivate")
    public ResponseEntity<GApiResponse<ShopResponse>> deActivateShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @PathVariable("shopId") Long shopId) {
        Shop shop = shopService.deActivateShop(shopId);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    /**
     * Ngừng kinh doanh 1 shop
     *
     * @param userPrincipal
     * @param shopId
     * @return
     */
    @PreAuthorizeAccessGeneralManagerToShop
    @GetMapping("/{shopId}/activate")
    public ResponseEntity<GApiResponse<ShopResponse>> activateShop(@ApiIgnore
                                                                   @CurrentUser UserPrincipal userPrincipal, @PathVariable("shopId") Long shopId) {
        Shop shop = shopService.activateShop(shopId);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorizeAccessGeneralManagerToShop
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<ShopResponse>>> activateShops(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<Shop> shops = shopService.activateShops(request.getIds());
        List<ShopResponse> responses = shops.stream().map(ShopResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorizeAccessGeneralManagerToShop
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<ShopResponse>>> deactivateShops(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<Shop> shops = shopService.deactivateShops(request.getIds());
        List<ShopResponse> responses = shops.stream().map(ShopResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    /**
     * Lấy danh sách nhân viên của 1 cửa hàng
     *
     * @param userPrincipal
     * @param shopId
     * @return
     */
    @PreAuthorizeAccessGeneralManagerToShop
    @GetMapping("/{shopId}/getListStaff")
    public ResponseEntity<GApiResponse<List<User>>> getListStaff(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "shopId") Long shopId) {
        List<User> listStaff = shopService.getListStaffOfShop(shopId);
        return ResponseEntity.ok(GApiResponse.success(listStaff));
    }


    @PreAuthorizeAccessGeneralManagerToShop
    @GetMapping("/{shopId}/linkWarehouse")
    public ResponseEntity<GApiResponse<Boolean>> linkWarehouseToShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @PathVariable(name = "shopId") Long shopId, @RequestParam(name = "warehouseId") Long warehouseId) {
        shopService.linkWarehouseToShop(userPrincipal.getId(), warehouseId, shopId);
        return ResponseEntity.ok(GApiResponse.success(true));
    }

    @PreAuthorizeAccessGeneralManagerToShop
    @GetMapping("/{shopId}/linkSaleChannel")
    public ResponseEntity<GApiResponse<Boolean>> linkSaleChannelToShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal,
                                                                       @PathVariable(name = "shopId") Long shopId,
                                                                       @RequestParam(name = "saleChannelId") Long saleChannelId) {
        shopService.linkSaleChannelToShop(userPrincipal.getId(), saleChannelId, shopId);
        return ResponseEntity.ok(GApiResponse.success(true));
    }

    //End API

    /*
     *  Lấy danh sách shop có quyền quản lý
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getCreatedShop")
    public ResponseEntity<GApiResponse<List<ShopResponse>>> getCreatedShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal) {
        List<Shop> shops = shopService.getCreatedShop(userPrincipal.getId());
        List<ShopResponse> responses = shops.stream().map(ShopResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
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
    public ResponseEntity<GApiResponse<Boolean>> givePermissionManageShop(@ApiIgnore @CurrentUser UserPrincipal userPrincipal, @RequestParam("userId") Long userId, @RequestParam("shopId") Long shopId) {
        if (userPrincipal.getParent() != null) {
            //Không cho phép truy cập nếu không phải là tổng quản lý
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PERMISSION_DENIED));
        }
        shopService.givePermissionManageShop(userId, shopId);
        return ResponseEntity.ok(GApiResponse.success(true));
    }


}
