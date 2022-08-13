package com.wifosell.zeus.payload.request.ecom_sync;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LazadaLinkCategoryRequest {
    @NotNull
    private Long sysCategoryId;
    private Long lazadaCategoryId;
}
