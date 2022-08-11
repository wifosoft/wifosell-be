package com.wifosell.lazada.modules.product.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.wifosell.lazada.utils.LazadaConvertUtils;

import java.util.List;

@JacksonXmlRootElement(localName = "Request")
public class LazadaUpdatePriceQuantityRequest {
    @JacksonXmlProperty(localName = "Product")
    Product product;

    public LazadaUpdatePriceQuantityRequest(List<Sku> skus) {
        product = new Product(skus);
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
        String itemId;
        @JacksonXmlProperty(localName = "SkuId")
        String skuId;
        @JacksonXmlProperty(localName = "SellerSku")
        String sellerSku;
        @JacksonXmlProperty(localName = "Price")
        String price;
        @JacksonXmlProperty(localName = "SalePrice")
        String salePrice;
        @JacksonXmlProperty(localName = "SaleStartDate")
        String saleStartDate;
        @JacksonXmlProperty(localName = "SaleEndDate")
        String saleEndDate;
        @JacksonXmlProperty(localName = "Quantity")
        String quantity;

        public Sku(String itemId, String skuId, String sellerSku, String price, String salePrice, String saleStartDate, String saleEndDate, String quantity) {
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

    public String toXmlString() throws JsonProcessingException {
        return LazadaConvertUtils.fromObjectToXmlString(this);
    }
}
