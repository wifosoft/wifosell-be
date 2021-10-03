package com.wifosell.zeus.annotation;

import com.wifosell.zeus.security.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated() and @userService.hasAccessToUser(#userPrincipal, #userId)")
//@PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or @userService.hasAccessToUser(#userPrincipal, #userId))")
public @interface PreAuthorizeAccessToUser {
}
