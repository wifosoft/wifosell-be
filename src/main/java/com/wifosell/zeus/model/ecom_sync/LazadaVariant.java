package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.wifosell.lazada.modules.product.payload.LazadaGetProductItemResponse;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.payload.provider.lazada.ResponseListProductPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "lazada_variants")
public class LazadaVariant extends BasicEntity {

    @JsonProperty("variant_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_sku")
    private String sellerSku;

    @Column(name = "shop_sku")
    private String shopSku;

    @Column(name = "status")
    private String status;

    @Column(name = "url")
    private String url;

    @Column(name = "special_price")
    private BigDecimal specialPrice;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "sku_id")
    private Long skuId;

    @Column(columnDefinition = "TEXT", name = "images")
    private String images;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sellable_stock")
    private Integer sellableStock;

    @Column(columnDefinition = "TEXT", name = "raw_data")
    private String rawData;

    @Column(columnDefinition = "TEXT", name = "reserved")
    private String reserved;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = true)
    private LazadaProduct lazadaProduct;

    @Column(name = "item_id", insertable = false, updatable = false)
    private Long lazadaProductId;


    public LazadaVariant() {
    }

    public LazadaVariant(ResponseListProductPayload.Sku s) {
        this.withDataBySkuAPI(s);
    }

    public LazadaVariant(ResponseListProductPayload.Sku s, LazadaProduct lazadaProduct) {
        this.withDataBySkuAPI(s);
        this.setLazadaProduct(lazadaProduct);
    }

    public LazadaVariant withDataBySkuAPI(ResponseListProductPayload.Sku s) {
        Gson gson = new Gson();
        this.sellerSku = s.getSellerSku();
        this.shopSku = s.getShopSku();
        this.status = s.getStatus();
        this.url = s.getUrl();
        this.specialPrice = s.getSpecial_price();
        this.price = s.getPrice();
        this.skuId = s.getSkuId();
        this.images = gson.toJson(s.getImages());
        this.quantity = s.getQuantity();
        this.sellableStock = s.getSellableStock();
        this.rawData = gson.toJson(s);
        this.reserved = "";
        return this;
    }

    public void injectData(LazadaGetProductItemResponse.Data.Sku sku) {
        Gson gson = new Gson();
        this.sellerSku = sku.getSellerSku();
        this.shopSku = sku.getShopSku();
        this.status = sku.getStatus();
        this.url = sku.getUrl();
        this.specialPrice = sku.getSpecialPrice();
        this.price = sku.getPrice();
        this.skuId = sku.getSkuId();
        this.images = gson.toJson(sku.getImages());
        this.quantity = sku.getQuantity();
        this.sellableStock = sku.getAvailable();
        this.rawData = gson.toJson(sku);
        this.reserved = "";
    }
}
