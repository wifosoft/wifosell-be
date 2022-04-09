package com.wifosell.zeus.payload.response.customer;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
public class CustomerResponse extends BasicEntityResponse {
    private final String fullName;
    private final String dob;
    private final String sex;
    private final String phone;
    private final String email;
    private final String cin;
    private final String nation;
    private final String city;
    private final String district;
    private final String ward;
    private final String addressDetail;

    public CustomerResponse(Customer customer) {
        super(customer);
        this.fullName = customer.getFullName();
        this.dob = new SimpleDateFormat("yyyy-MM-dd").format(customer.getDob());
        this.sex = customer.getSex().toString();
        this.phone = customer.getPhone();
        this.email = customer.getEmail();
        this.cin = customer.getCin();
        this.nation = customer.getNation();
        this.city = customer.getCity();
        this.district = customer.getDistrict();
        this.ward = customer.getWard();
        this.addressDetail = customer.getAddressDetail();
    }
}
