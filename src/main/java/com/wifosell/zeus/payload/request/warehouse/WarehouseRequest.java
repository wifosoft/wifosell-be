package com.wifosell.zeus.payload.request.warehouse;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter

public class WarehouseRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String shortName;

    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String description;

    private Boolean active;
}
