package com.wifosell.zeus.payload.request.warehouse;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class ActivateWarehousesRequest {
    @NotEmpty
    List<Long> ids;
}
