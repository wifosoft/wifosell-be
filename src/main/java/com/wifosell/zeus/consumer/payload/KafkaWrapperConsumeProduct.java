package com.wifosell.zeus.consumer.payload;

import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class KafkaWrapperConsumeProduct {
    UpdateProductRequest updateProductRequest; //use for update/create product
    List<KafkaWrapperConsumeProductVariantShortInfo> listVariants;

    public KafkaWrapperConsumeProduct() {
        updateProductRequest = new UpdateProductRequest();
        listVariants = new ArrayList<>();
    }

    public List<String> mapVariantSkus() {
        return listVariants.stream().map(e -> e.getSku()).collect(Collectors.toList());
    }

}

