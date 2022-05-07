package com.wifosell.zeus.payload.request.supplier;

public interface ISupplierRequest {
    String getName();

    String getPhone();

    String getEmail();

    String getNation();

    String getCity();

    String getDistrict();

    String getWard();

    String getAddressDetail();

    Boolean getIsActive();
}
