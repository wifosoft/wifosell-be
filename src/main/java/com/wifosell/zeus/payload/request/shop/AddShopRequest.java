package com.wifosell.zeus.payload.request.shop;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class AddShopRequest implements IShopRequest {
    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String shortName;

    @NotBlank
    @Size(max = 300)
    private String address;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotBlank
    @Size(max = 50)
    private String businessLine;

    private List<Long> saleChannelIds;

    private Boolean isActive;
}
