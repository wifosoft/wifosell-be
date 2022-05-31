package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.payload.provider.lazada.ResponseListProductPayload;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "lazada_products")
public class LazadaProduct extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String createdTime;
    private String updatedTime;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "primary_category")
    private Long primaryCategory;

    @Column(name = "name")
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT", name = "item_data")
    private String itemData;

    @Column(name = "sku_count", columnDefinition = "int default 0")
    private Integer skuCount;

    @Column(columnDefinition = "TEXT", name = "images")
    private String images;

    @OneToMany(mappedBy = "lazadaProduct")
    private List<LazadaVariant> lazadaVariants;

    @ManyToOne
    @JoinColumn(name = "ecom_account_id", nullable = true)
    private EcomAccount ecomAccount;


    public LazadaProduct() {}
    public LazadaProduct(ResponseListProductPayload.Product e){
        this.withDataByProductAPI(e);
    }
    public LazadaProduct(ResponseListProductPayload.Product e, EcomAccount ecomAccount){
        this.withDataByProductAPI(e);
        this.setEcomAccount(ecomAccount);
    }

    public LazadaProduct withDataByProductAPI(ResponseListProductPayload.Product e){
        Gson gson = new Gson();
         this.name = e.getAttributes().getName();
         this.itemId = e.getItem_id();
         this.primaryCategory  =e.getPrimary_category();
         this.itemData = gson.toJson(e);
         this.skuCount = e.getSkus().size();
         this.images = gson.toJson(e.getImages());
         this.createdTime =  e.getCreated_time();
         this.updatedTime = e.getUpdated_time();
         return this;
    }
}
