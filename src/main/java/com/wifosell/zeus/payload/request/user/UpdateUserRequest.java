package com.wifosell.zeus.payload.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class UpdateUserRequest {

    @NotBlank
    @Size(max = 40)
    private String firstName;

    @Size(max = 40)
    private String lastName;

    @Size(max = 40)
    @Email
    private String email;

    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String phone;

}
