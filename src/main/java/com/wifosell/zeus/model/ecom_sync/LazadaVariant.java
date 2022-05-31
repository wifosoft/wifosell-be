package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.*;
import org.hibernate.validator.constraints.CodePointLength;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "lazada_variants")
public class LazadaVariant extends BasicEntity {
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
    private Integer quantity;

    @Column(name = "sellable_stock")
    private Integer sellableStock;

    @Column(columnDefinition = "TEXT", name = "raw_data")
    private String rawData;

    @Column(columnDefinition = "TEXT", name = "reserved")
    private String reserved;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = true)
    private LazadaProduct lazadaProduct;

}
