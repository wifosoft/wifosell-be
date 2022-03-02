package com.wifosell.zeus.payload.response.product;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.product.Attribute;
import com.wifosell.zeus.model.product.OptionProductRelation;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponse extends BasicEntityResponse {
    private final Long id;
    private final String name;
    private final String sku;
    private final String barcode;
    private final Integer weight;
    private final String dimension;
    private final Integer state;
    private final Integer status;
    private final List<Attribute> attributes;
    private final List<OptionModel> options;
    private final List<Variant> variants;

    public ProductResponse(Product product) {
        super(product);
        this.id = product.getId();
        this.name = product.getName();
        this.sku = product.getSku();
        this.barcode = product.getBarcode();
        this.weight = product.getWeight();
        this.dimension = product.getDimension();
        this.state = product.getState();
        this.status = product.getStatus();
        this.attributes = product.getAttributes();
        this.options = product.getOptionProductRelations()
                .stream().map(OptionProductRelation::getOption).collect(Collectors.toList());
        this.variants = product.getVariants();
    }
}
