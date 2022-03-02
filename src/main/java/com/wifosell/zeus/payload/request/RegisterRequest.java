package com.wifosell.zeus.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
public class RegisterRequest {
    @NotBlank
    @Size(max = 40)
    private String firstName;

    @NotBlank
    @Size(max = 40)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 15)
    private String userName;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 20)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotBlank
    @Size(max = 255)
    private String address;
}
