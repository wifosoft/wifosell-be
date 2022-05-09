package com.wifosell.zeus.payload.request.supplier;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSupplierRequest implements ISupplierRequest {
    String name;

    String phone;

    String email;

    String nation;

    String city;

    String district;

    String ward;

    String addressDetail;

    Boolean isActive;
}
