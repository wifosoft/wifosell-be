package com.wifosell.zeus.security;


import io.swagger.annotations.Api;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
