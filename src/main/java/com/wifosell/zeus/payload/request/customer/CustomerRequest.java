package com.wifosell.zeus.payload.request.customer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerRequest {
    @NotBlank
    private String fullname;
}
