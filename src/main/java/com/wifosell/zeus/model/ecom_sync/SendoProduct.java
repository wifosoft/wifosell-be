package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "sendo_products")
public class SendoProduct extends BasicEntity {
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


    @Column(name = "product_identify")
    private String productIdentify;

    @Column(name = "sku_count", columnDefinition = "int default 0")
    private Integer skuCount;

    @Column(columnDefinition = "TEXT", name = "images")
    private String images;

    @JsonProperty("variants")
    @OneToMany(mappedBy = "sendoProduct", cascade = CascadeType.ALL)
    private List<SendoVariant> sendoVariants;

    @ManyToOne
    @JoinColumn(name = "ecom_account_id", nullable = true)
    private EcomAccount ecomAccount;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;



    public SendoProduct() {
    }

    public SendoProduct(ResponseSendoProductItemPayload e) {
        this.withDataByProductAPI(e);
    }

    public SendoProduct(ResponseSendoProductItemPayload e, EcomAccount ecomAccount) {
        this.withDataByProductAPI(e);
        this.setEcomAccount(ecomAccount);
        this.setGeneralManager(ecomAccount.getGeneralManager());
    }

    public SendoProduct withDataByProductAPI(ResponseSendoProductItemPayload e) {
        Gson gson = new Gson();

        this.productIdentify   = e.getSku();
        this.name = e.getName();
        this.itemId = e.getId();
        this.primaryCategory = e.getCat_4_id();
        this.itemData = gson.toJson(e);
        this.skuCount = e.getVariants().size();

        if (e.getImage() != null) {
            this.images = gson.toJson(new String[]{e.getImage()});
        }
        //Optional.ofNullable(request.getDescription()).ifPresent(product::setDescription);
        return this;
    }
}
