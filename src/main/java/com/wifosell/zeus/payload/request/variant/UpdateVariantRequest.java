package com.wifosell.zeus.payload.request.variant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVariantRequest implements IVariantRequest {
    private Boolean isActive;
}
