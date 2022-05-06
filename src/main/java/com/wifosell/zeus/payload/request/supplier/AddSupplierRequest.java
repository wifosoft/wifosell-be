package com.wifosell.zeus.payload.request.supplier;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AddSupplierRequest implements ISupplierRequest {
    @NotBlank
    String name;

    @NotBlank
    String phone;

    @Email
    String email;

    String nation;

    String city;

    String district;

    String ward;

    String addressDetail;

    Boolean active;
}
