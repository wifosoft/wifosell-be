package com.wifosell.zeus.payload.request.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ChangeRoleRequest {
    @NotNull
    List<String> listRoleString;
}
