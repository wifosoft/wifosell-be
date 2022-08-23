package com.wifosell.zeus.payload.response.product;

import com.wifosell.zeus.model.product.ProductImage;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

@Getter
public class ProductImageResponse extends BasicEntityResponse {
    private final String url;

    public ProductImageResponse(ProductImage image) {
        super(image);
        this.url = image.getUrl();
    }
}
