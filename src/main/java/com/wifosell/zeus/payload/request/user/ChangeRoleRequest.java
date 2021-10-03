package com.wifosell.zeus.payload.request.user;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChangeRoleRequest {
    @NonNull
    List<String> listRoleString;
}
