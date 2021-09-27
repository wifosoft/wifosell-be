package com.wifosell.zeus.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomSecurityUserDetailsService {
    UserDetails loadUserByUsername(String usernameOrdEmail) throws UsernameNotFoundException;

    UserDetails loadUserById(Long id);

}
