package com.wifosell.zeus.payload.request.shop;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ShopRequest {
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

    @Size(max = 50)
    private String businessLine;

    private Boolean isActive;

    private List<Long> saleChannelIds;
}
