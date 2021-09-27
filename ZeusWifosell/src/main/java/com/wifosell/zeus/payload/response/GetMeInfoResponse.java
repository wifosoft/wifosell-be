package com.wifosell.zeus.payload.response;

import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.user.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@Builder
public class GetMeInfoResponse {

    private String firstName;
    private String lastName;
    private String userName;
    private String createdAt;
    private String updatedAt;
    private String phone;

    private User parent;
    private Collection<String> authorities;

    private String email;

}
