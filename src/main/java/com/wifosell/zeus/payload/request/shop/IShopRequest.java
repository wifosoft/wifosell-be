package com.wifosell.zeus.payload.request.shop;

import java.util.List;

public interface IShopRequest {
    String getName();

    String getShortName();

    String getAddress();

    String getPhone();

    String getDescription();

    String getBusinessLine();

    List<Long> getSaleChannelIds();

    Boolean getIsActive();
}
