package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "sendo_variants")
public class SendoVariant extends BasicEntity {

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
    private Long specialPrice;

    @Column(name = "price")
    private Long price;

    @Column(name = "skuId")
    private Long skuId;

    @Column(columnDefinition = "TEXT", name = "images")
    private String images;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "sellable_stock")
    private Long sellableStock;

    @Column(columnDefinition = "TEXT", name = "raw_data")
    private String rawData;

    @Column(columnDefinition = "TEXT", name = "reserved")
    private String reserved;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = true)
    private SendoProduct sendoProduct;

    @Column(name = "item_id", insertable = false, updatable = false)
    private Long sendoProductId;


    public SendoVariant() {
    }

    public SendoVariant(ResponseSendoProductItemPayload.Variant s) {
        this.withDataBySkuAPI(s);
    }

    public SendoVariant(ResponseSendoProductItemPayload.Variant s, SendoProduct sendoProduct) {
        this.withDataBySkuAPI(s);
        this.setSendoProduct(sendoProduct);
    }

    public SendoVariant withDataBySkuAPI(ResponseSendoProductItemPayload.Variant s) {
        Gson gson = new Gson();
        this.sellerSku = s.getVariant_sku();
        this.shopSku = s.getVariant_attribute_hash();
        this.status = "active";
        this.url = "";
        this.specialPrice = s.getVariant_special_price();
        this.price = s.getVariant_price();
        this.skuId = -1L;
        this.images = "[]";
        this.quantity = s.getVariant_quantity();
        this.sellableStock = s.getVariant_quantity();
        this.rawData = gson.toJson(s);
        this.reserved = "";
        return this;
    }

}
