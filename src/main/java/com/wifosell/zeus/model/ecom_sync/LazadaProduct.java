package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
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
}
