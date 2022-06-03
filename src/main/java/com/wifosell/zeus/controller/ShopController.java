package com.wifosell.zeus.controller;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.shop.AddShopRequest;
import com.wifosell.zeus.payload.request.shop.UpdateShopRequest;
import com.wifosell.zeus.payload.response.shop.ShopResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.Shop2Service;
import com.wifosell.zeus.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {
    private final Shop2Service shop2Service;
    private final ShopService shopService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<Page<ShopResponse>>> getAllShops(
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<Shop> shops = shop2Service.getShops(null, isActives, offset, limit, sortBy, orderBy);
        Page<ShopResponse> responses = shops.map(ShopResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<ShopResponse>>> getShops(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<Shop> shops = shop2Service.getShops(userPrincipal.getId(), isActives, offset, limit, sortBy, orderBy);
        Page<ShopResponse> responses = shops.map(ShopResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{shopId}")
    public ResponseEntity<GApiResponse<ShopResponse>> getShop(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "shopId") Long shopId
    ) {
        Shop shop = shop2Service.getShop(userPrincipal.getId(), shopId);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<ShopResponse>> addShop(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid AddShopRequest request
    ) {
        Shop shop = shop2Service.addShop(userPrincipal.getId(), request);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{shopId}/update")
    public ResponseEntity<GApiResponse<ShopResponse>> updateShop(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "shopId") Long shopId,
            @RequestBody @Valid UpdateShopRequest request
    ) {
        Shop shop = shop2Service.updateShop(userPrincipal.getId(), shopId, request);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{shopId}/activate")
    public ResponseEntity<GApiResponse<ShopResponse>> activateShop(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "shopId") Long shopId
    ) {
        Shop shop = shop2Service.activateShop(userPrincipal.getId(), shopId);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{shopId}/deactivate")
    public ResponseEntity<GApiResponse<ShopResponse>> deactivateShop(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "shopId") Long shopId
    ) {
        Shop shop = shop2Service.deactivateShop(userPrincipal.getId(), shopId);
        ShopResponse response = new ShopResponse(shop);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<ShopResponse>>> activateShops(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<Shop> shops = shop2Service.activateShops(userPrincipal.getId(), request.getIds());
        List<ShopResponse> responses = shops.stream().map(ShopResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<ShopResponse>>> deactivateShops(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<Shop> shops = shop2Service.deactivateShops(userPrincipal.getId(), request.getIds());
        List<ShopResponse> responses = shops.stream().map(ShopResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

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
