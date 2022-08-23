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

    @Size(max = 50)
    private String shortName;

    @Size(max = 300)
    private String address;

    @Size(max = 20)
    private String phone;

    @Size(max = 1000)
    private String description;
    
    @Size(max = 50)
    private String businessLine;

    private List<Relation> relations;

    private Boolean isActive;
}
