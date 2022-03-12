package com.wifosell.zeus.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.shop.ProductShopRelation;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
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
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private Integer weight;

    @Size(max = 50)
    private String dimension;

    private Integer state = 0;

    private Integer status = 0;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OptionModel> options = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Variant> variants = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductShopRelation> productShopRelations = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
