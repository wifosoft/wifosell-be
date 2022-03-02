package com.wifosell.zeus.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.shop.ProductShopRelation;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String sku;

    @Size(max = 255)
    private String barcode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Integer weight;

    @Size(max = 50)
    private String dimension;

    private Integer state = 0;

    private Integer status = 0;

    @OneToMany(mappedBy = "product")
    private List<Attribute> attributes;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OptionProductRelation> optionProductRelations;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Variant> variants;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductShopRelation> productShopRelations;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
