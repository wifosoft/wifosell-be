package com.wifosell.zeus.payload.request.shop;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface IShopRequest {
    String getName();

    String getShortName();

    String getAddress();

    String getPhone();

    String getDescription();

    String getBusinessLine();

    List<Relation> getRelations();

    Boolean getIsActive();

    @Getter
    @Setter
    class Relation {
        @NotNull
        Long saleChannelId;

        @NotNull
        Long warehouseId;
    }
}
