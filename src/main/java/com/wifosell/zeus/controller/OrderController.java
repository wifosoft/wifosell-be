package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import com.wifosell.zeus.payload.response.order.OrderResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.OrderService;
import com.wifosell.zeus.utils.Preprocessor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<OrderResponse>>> getAllOrders(
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<OrderModel> orders = orderService.getAllOrders(isActive);
        List<OrderResponse> responses = orders.stream().map(OrderResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<OrderResponse>>> getOrders(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
            @RequestParam(name = "saleChannelId", required = false) List<Long> saleChannelIds,
            @RequestParam(name = "isActive", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<OrderModel> orders = orderService.getOrders(userPrincipal.getId(), isActive);
        if (shopIds != null) {
            orders = orders.stream()
                    .filter(order -> shopIds.contains(order.getShop().getId()))
                    .collect(Collectors.toList());
        }
        if (saleChannelIds != null) {
            orders = orders.stream()
                    .filter(order -> saleChannelIds.contains(order.getSaleChannel().getId()))
                    .collect(Collectors.toList());
        }
        List<OrderResponse> responses = orders.stream().map(OrderResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{orderId}")
    public ResponseEntity<GApiResponse<OrderResponse>> getOrder(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "orderId") Long orderId
    ) {
        OrderModel order = orderService.getOrder(userPrincipal.getId(), orderId);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<OrderResponse>> addOrder(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody AddOrderRequest request
    ) {
        OrderModel order = orderService.addOrder(userPrincipal.getId(), request);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{orderId}/update")
    public ResponseEntity<GApiResponse<OrderResponse>> updateProduct(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "orderId") Long orderId,
            @RequestBody UpdateOrderRequest request
    ) {
        OrderModel order = orderService.updateOrder(userPrincipal.getId(), orderId, request);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{orderId}/activate")
    public ResponseEntity<GApiResponse<OrderResponse>> activateOrder(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "orderId") Long orderId
    ) {
        OrderModel order = orderService.activateOrder(userPrincipal.getId(), orderId);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{orderId}/deactivate")
    public ResponseEntity<GApiResponse<OrderResponse>> deactivateOrder(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "orderId") Long orderId
    ) {
        OrderModel order = orderService.deactivateOrder(userPrincipal.getId(), orderId);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<OrderResponse>>> activateOrders(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<OrderModel> orders = orderService.activateOrders(userPrincipal.getId(), request.getIds());
        List<OrderResponse> responses = orders.stream().map(OrderResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<OrderResponse>>> deactivateOrders(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<OrderModel> orders = orderService.deactivateOrders(userPrincipal.getId(), request.getIds());
        List<OrderResponse> responses = orders.stream().map(OrderResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
