package com.wifosell.zeus.payload.response.supplier;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

@Getter
public class SupplierResponse extends BasicEntityResponse {
    private final String name;
    private final String phone;
    private final String email;
    private final String nation;
    private final String city;
    private final String district;
    private final String ward;
    private final String addressDetail;

    public SupplierResponse(Supplier supplier) {
        super(supplier);
        this.name = supplier.getName();
        this.phone = supplier.getPhone();
        this.email = supplier.getEmail();
        this.nation = supplier.getNation();
        this.city = supplier.getCity();
        this.district = supplier.getDistrict();
        this.ward = supplier.getWard();
        this.addressDetail = supplier.getAddressDetail();
    }
}
