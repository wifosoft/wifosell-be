package com.wifosell.zeus.payload.request.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wifosell.zeus.model.customer.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class CustomerRequest {
    @NotBlank
    private String fullName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;   // Date of birth

    @Enumerated(EnumType.ORDINAL)
    private Customer.Sex sex;

    private String phone;

    private String email;

    private String cin; // Citizen Identification Number

    private String nation;

    private String city;

    private String district;

    private String ward;

    private String addressDetail;

    private Boolean active;
}
