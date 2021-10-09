package com.wifosell.zeus.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated() and @userService.hasAccessGeneralManagerToShop(#userPrincipal, #shopId)")
//@PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or @userService.hasAccessToUser(#userPrincipal, #userId))")
public @interface PreAuthorizeAccessGeneralManagerToShop {
}
