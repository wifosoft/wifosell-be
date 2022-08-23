package com.wifosell.zeus.payload.request.variant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddVariantRequest implements IVariantRequest {
    private Boolean isActive;
}
