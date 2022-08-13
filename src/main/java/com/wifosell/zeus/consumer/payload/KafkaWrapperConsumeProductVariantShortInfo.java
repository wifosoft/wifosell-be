package com.wifosell.zeus.consumer.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaWrapperConsumeProductVariantShortInfo {
    public String sku;
    public Long price;
    public Long specialPrice;
    public Long quantity;
}
