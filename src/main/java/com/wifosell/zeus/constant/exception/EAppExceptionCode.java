package com.wifosell.zeus.constant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public enum EAppExceptionCode {
    UNEXPECTED_ERROR(-1),
    RECORD_EXISTED(-2),
    //User
    USER_NOT_FOUND(100001),
    USER_NOT_IN_RELEVANT_SHOP(100002),
    USERNAME_HAS_BEEN_TAKEN(100003),
    EMAIL_HAS_BEEN_TAKEN(100004),
    USER_OLD_PASSWORD_INCORRECT(100005),
    USER_VERIFIED_PASSWORD_INCORRECT(100006),


    //permission
    PERMISSION_DENIED(400001),
    PERMISSION_NOT_FOUND(400002),
    //Role
    ROLE_NOT_FOUND(500001),

    //Warehouse
    WAREHOUSE_NOT_FOUND(600001),
    //shop
    SHOP_NOT_FOUND(200001),
    SHOP_MANAGED_BY_THIS_USER(200002),
    SHOP_ADD_PERMISSION_BY_CHILD_USER(200003),

    // Category
    CATEGORY_NOT_FOUND(700001),
    PARENT_CATEGORY_NOT_FOUND(700002),

    //region Sale Channel
    SALE_CHANNEL_NOT_FOUND(800001);
    //endregion

    private final int value;

    EAppExceptionCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
