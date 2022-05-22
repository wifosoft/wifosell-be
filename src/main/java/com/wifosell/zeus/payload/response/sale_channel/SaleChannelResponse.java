package com.wifosell.zeus.payload.response.sale_channel;

import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

@Getter
public class SaleChannelResponse extends BasicEntityResponse {
    private final String name;
    private final String shortName;
    private final String description;

    public SaleChannelResponse(SaleChannel saleChannel) {
        super(saleChannel);
        this.name = saleChannel.getName();
        this.shortName = saleChannel.getShortName();
        this.description = saleChannel.getDescription();
    }
}
