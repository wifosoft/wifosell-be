package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderPaymentStatusRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderRequest;
import com.wifosell.zeus.payload.request.order.UpdateOrderStatusRequest;
import com.wifosell.zeus.payload.response.order.OrderResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<Page<OrderResponse>>> getAllOrders(
            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
            @RequestParam(name = "saleChannelId", required = false) List<Long> saleChannelIds,
            @RequestParam(name = "status", required = false) List<OrderModel.STATUS> statuses,
            @RequestParam(name = "paymentMethod", required = false) List<Payment.METHOD> paymentMethods,
            @RequestParam(name = "paymentStatus", required = false) List<Payment.STATUS> paymentStatues,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<OrderModel> orders = orderService.getOrders(
                null, shopIds, saleChannelIds,
                statuses, paymentMethods, paymentStatues,
                isActives, offset, limit, sortBy, orderBy);
        Page<OrderResponse> responses = orders.map(OrderResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<OrderResponse>>> getOrders(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "shopId", required = false) List<Long> shopIds,
            @RequestParam(name = "saleChannelId", required = false) List<Long> saleChannelIds,
            @RequestParam(name = "status", required = false) List<OrderModel.STATUS> statuses,
            @RequestParam(name = "paymentMethod", required = false) List<Payment.METHOD> paymentMethods,
            @RequestParam(name = "paymentStatus", required = false) List<Payment.STATUS> paymentStatues,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<OrderModel> orders = orderService.getOrders(
                userPrincipal.getId(), shopIds, saleChannelIds,
                statuses, paymentMethods, paymentStatues,
                isActives, offset, limit, sortBy, orderBy);
        Page<OrderResponse> responses = orders.map(OrderResponse::new);
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
            @RequestBody @Valid AddOrderRequest request
    ) {
        OrderModel order = orderService.addOrder(userPrincipal.getId(), request);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{orderId}/update")
    public ResponseEntity<GApiResponse<OrderResponse>> updateOrder(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "orderId") Long orderId,
            @RequestBody @Valid UpdateOrderRequest request
    ) {
        OrderModel order = orderService.updateOrder(userPrincipal.getId(), orderId, request);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{orderId}/updateStatus")
    public ResponseEntity<GApiResponse<OrderResponse>> updateOrderStatus(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "orderId") Long orderId,
            @RequestBody @Valid UpdateOrderStatusRequest request
    ) {
        OrderModel order = orderService.updateOrderStatus(userPrincipal.getId(), orderId, request);
        OrderResponse response = new OrderResponse(order);
        return ResponseEntity.ok(GApiResponse.success(response));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{orderId}/payment/updateStatus")
    public ResponseEntity<GApiResponse<OrderResponse>> updateOrderPaymentStatus(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "orderId") Long orderId,
            @RequestBody @Valid UpdateOrderPaymentStatusRequest request
    ) {
        OrderModel order = orderService.updateOrderPaymentStatus(userPrincipal.getId(), orderId, request);
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
