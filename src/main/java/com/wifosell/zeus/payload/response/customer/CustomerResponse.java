package com.wifosell.zeus.payload.response.customer;

import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Optional;

@Getter
public class CustomerResponse extends BasicEntityResponse {
    private String fullName;
    private String dob;
    private String sex;
    private String phone;
    private String email;
    private String cin;
    private String nation;
    private String city;
    private String district;
    private String ward;
    private String addressDetail;

    public CustomerResponse(Customer customer) {
        super(customer);
        this.fullName = customer.getFullName();

        Optional.ofNullable(customer.getDob()).ifPresent(e -> {
            try {
                this.dob = new SimpleDateFormat("yyyy-MM-dd").format(customer.getDob());
            } catch (Exception exception) {
            }
        });
        Optional.ofNullable(customer.getSex()).ifPresent(e -> {
            this.sex = customer.getSex().toString();
        });

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
