package com.wifosell.zeus.payload.response.warehouse;

import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

@Getter
public class WarehouseResponse extends BasicEntityResponse {
    private final String name;
    private final String shortName;
    private final String address;
    private final String phone;
    private final String description;

    public WarehouseResponse(Warehouse warehouse) {
        super(warehouse);
        this.name = warehouse.getName();
        this.shortName = warehouse.getShortName();
        this.address = warehouse.getAddress();
        this.phone = warehouse.getPhone();
        this.description = warehouse.getDescription();
    }
}
