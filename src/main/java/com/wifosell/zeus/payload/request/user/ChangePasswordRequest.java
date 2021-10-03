package com.wifosell.zeus.payload.request.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePasswordRequest {

    @Size(max=20)
    String oldPassword;

    @NotBlank
    @Size(min=4, max =20)
    String newPassword;

    @NotBlank
    @Size(min=4, max =20)
    String verifyPassword;
}
