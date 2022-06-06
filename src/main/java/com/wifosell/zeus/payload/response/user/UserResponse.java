package com.wifosell.zeus.payload.response.user;

import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

@Getter
public class UserResponse extends BasicEntityResponse {
    private final String firstName;
    private final String lastName;
    private final String userName;
    private final String email;
    private final String avatar;
    private final String address;
    private final String phone;

    public UserResponse(User user) {
        super(user);
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getUsername();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.address = user.getAddress();
        this.phone = user.getPhone();
    }
}
