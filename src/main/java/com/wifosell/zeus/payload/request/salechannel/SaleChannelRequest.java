package com.wifosell.zeus.payload.request.salechannel;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter

public class SaleChannelRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String shortName;

    private String description;

}
