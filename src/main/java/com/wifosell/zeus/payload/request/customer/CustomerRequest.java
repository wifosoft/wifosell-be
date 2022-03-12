package com.wifosell.zeus.payload.request.customer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CustomerRequest {
    @NotBlank
    private String fullname;
}
