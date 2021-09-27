package com.wifosell.zeus.constant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public enum EAppExceptionCode {
    //SHOP
    USER_NOT_FOUND(100001),
    USER_NOT_IN_RELEVANT_SHOP(100002),
    PERMISSION_DENIED(400001),

    SHOP_NOT_FOUND(200001),
    SHOP_MANAGED_BY_THIS_USER(200002);


    private final int value;

    EAppExceptionCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
