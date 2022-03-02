package com.wifosell.zeus.security;


import lombok.NonNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.annotation.*;

@Target({  ElementType.FIELD, ElementType.LOCAL_VARIABLE,  ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NonNull
@AuthenticationPrincipal
@ApiIgnore
public @interface CurrentUser {
}
