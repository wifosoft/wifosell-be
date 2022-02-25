package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<Customer>>> getAllCustomers() {
        List<Customer> customerList = customerService.getAllCustomers();
        return ResponseEntity.ok(GApiResponse.success(customerList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<Customer>>> getCustomers(@CurrentUser UserPrincipal userPrincipal) {
        List<Customer> customerList = customerService.getCustomersByUserId(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(customerList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/shop={shopId}")
    public ResponseEntity<GApiResponse<List<Customer>>> getCustomersByShopId(@CurrentUser UserPrincipal userPrincipal,
                                                                           @PathVariable(name = "shopId") Long shopId) {
        List<Customer> customerList = customerService.getCustomersByShopId(shopId);
        return ResponseEntity.ok(GApiResponse.success(customerList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{customerId}")
    public ResponseEntity<GApiResponse<Customer>> getCustomer(@CurrentUser UserPrincipal userPrincipal,
                                                            @PathVariable(name = "customerId") Long customerId) {
        Customer customer = customerService.getCustomer(customerId);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<Customer>> addCustomer(@CurrentUser UserPrincipal userPrincipal,
                                                            @RequestBody CustomerRequest customerRequest) {
        Customer customer = customerService.addCustomer(userPrincipal.getId(), customerRequest);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{customerId}/update")
    public ResponseEntity<GApiResponse<Customer>> addCustomer(@CurrentUser UserPrincipal userPrincipal,
                                                            @PathVariable(name = "customerId") Long customerId,
                                                            @RequestBody CustomerRequest customerRequest) {
        Customer customer = customerService.updateCustomer(customerId, customerRequest);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{customerId}/activate")
    public ResponseEntity<GApiResponse<Customer>> activateCustomer(@CurrentUser UserPrincipal userPrincipal,
                                                                 @PathVariable(name = "customerId") Long customerId) {
        Customer customer = customerService.activateCustomer(customerId);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{customerId}/deactivate")
    public ResponseEntity<GApiResponse<Customer>> deactivateCustomer(@CurrentUser UserPrincipal userPrincipal,
                                                                   @PathVariable(name = "customerId") Long customerId) {
        Customer customer = customerService.deactivateCustomer(customerId);
        return ResponseEntity.ok(GApiResponse.success(customer));
    }
}
