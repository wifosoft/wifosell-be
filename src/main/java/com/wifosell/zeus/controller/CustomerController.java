package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.CustomerService;
import com.wifosell.zeus.utils.Preprocessor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<Customer>>> getAllCustomers(
            @RequestParam(name = "active", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<Customer> customers = customerService.getAllCustomers(isActive);
        return ResponseEntity.ok(GApiResponse.success(customers));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<Customer>>> getCustomers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "active", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<Customer> customers = customerService.getCustomers(userPrincipal.getId(), isActive);
        return ResponseEntity.ok(GApiResponse.success(customers));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{customerId}")
    public ResponseEntity<GApiResponse<Customer>> getCustomer(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "customerId") Long customerId
    ) {
        Customer customer = customerService.getCustomer(userPrincipal.getId(), customerId);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<GApiResponse<Customer>> addCustomer(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody CustomerRequest request
    ) {
        Customer customer = customerService.addCustomer(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{customerId}/update")
    public ResponseEntity<GApiResponse<Customer>> addCustomer(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "customerId") Long customerId,
            @RequestBody CustomerRequest request
    ) {
        Customer customer = customerService.updateCustomer(userPrincipal.getId(), customerId, request);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{customerId}/activate")
    public ResponseEntity<GApiResponse<Customer>> activateCustomer(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "customerId") Long customerId
    ) {
        Customer customer = customerService.activateCustomer(userPrincipal.getId(), customerId);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{customerId}/deactivate")
    public ResponseEntity<GApiResponse<Customer>> deactivateCustomer(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "customerId") Long customerId
    ) {
        Customer customer = customerService.deactivateCustomer(userPrincipal.getId(), customerId);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<Customer>>> activateCustomers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<Customer> customers = customerService.activateCustomers(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(customers));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<Customer>>> deactivateCustomers(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ListIdRequest request
    ) {
        List<Customer> customers = customerService.deactivateCustomers(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(customers));
    }
}
