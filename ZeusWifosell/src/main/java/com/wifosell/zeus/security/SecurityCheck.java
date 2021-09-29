package com.wifosell.zeus.security;

import com.wifosell.zeus.model.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component

public class SecurityCheck {


    public boolean check(Authentication authentication , Long id) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if(!userPrincipal.getUsername().equals("manager1")){
            return false;
        }
        if(id  == 1)
            return false;

        return true;
    }
}
