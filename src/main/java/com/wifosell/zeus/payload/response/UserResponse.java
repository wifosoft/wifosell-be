package com.wifosell.zeus.payload.response;

import com.wifosell.zeus.model.user.User;

import java.util.Collection;

public class UserResponse {
    private String firstName;
    private String lastName;
    private String userName;
    private String createdAt;
    private String updatedAt;
    private String phone;
    private String avatar;
    private User parent;
    private Collection<String> authorities;
    private String email;
}
