package com.wifosell.lazada.modules.product.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.wifosell.lazada.utils.LazadaConvertUtils;

import java.math.BigDecimal;
import java.util.List;

@JacksonXmlRootElement(localName = "Request")
public class LazadaUpdatePriceQuantityRequest {
    @JacksonXmlProperty(localName = "Product")
    Product product;

    public LazadaUpdatePriceQuantityRequest(List<Sku> skus) {
        product = new Product(skus);
    }

    public LazadaUpdatePriceQuantityRequest(long itemId, long skuId, int quantity) {
        product = new Product(List.of(new Sku(
                itemId,
                skuId,
                null,
                null,
                null,
                null,
                null,
                quantity
        )));
    }

    public static class Product {
        @JacksonXmlElementWrapper(localName = "Skus")
        @JacksonXmlProperty(localName = "Sku")
        List<Sku> skus;

        Product(List<Sku> skus) {
            this.skus = skus;
        }
    }

    public static class Sku {
        @JacksonXmlProperty(localName = "ItemId")
        Long itemId;
        @JacksonXmlProperty(localName = "SkuId")
        Long skuId;
        @JacksonXmlProperty(localName = "SellerSku")
        String sellerSku;
        @JacksonXmlProperty(localName = "Price")
        BigDecimal price;
        @JacksonXmlProperty(localName = "SalePrice")
        BigDecimal salePrice;
        @JacksonXmlProperty(localName = "SaleStartDate")
        String saleStartDate;
        @JacksonXmlProperty(localName = "SaleEndDate")
        String saleEndDate;
        @JacksonXmlProperty(localName = "Quantity")
        Integer quantity;

        public Sku(Long itemId, Long skuId, String sellerSku, BigDecimal price, BigDecimal salePrice, String saleStartDate, String saleEndDate, Integer quantity) {
            this.itemId = itemId;
            this.skuId = skuId;
            this.sellerSku = sellerSku;
            this.price = price;
            this.salePrice = salePrice;
            this.saleStartDate = saleStartDate;
            this.saleEndDate = saleEndDate;
            this.quantity = quantity;
        }
    }

    public String toXml() throws JsonProcessingException {
        return LazadaConvertUtils.fromObjectToXmlString(this);
    }
}
