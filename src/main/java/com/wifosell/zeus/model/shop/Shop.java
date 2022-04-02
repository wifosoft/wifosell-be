package com.wifosell.zeus.model.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(
        name = "shops",
        uniqueConstraints = @UniqueConstraint(columnNames = {"general_manager_id", "short_name"}))
public class Shop extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotBlank
    private String name;

    @Size(max = 50)
    @Column(name = "short_name")
    private String shortName;

    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String description;

    @Size(max = 50)
    private String businessLine;

    @JsonIgnore
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<UserShopRelation> userShopRelations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<WarehouseShopRelation> warehouseShopRelations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<SaleChannelShopRelation> saleChannelShopRelations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<ProductShopRelation> productShopRelations = new ArrayList<>();

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;

    /*
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonManagedReference
    @ManyToMany(mappedBy = "shops")
    List<User> staffOfShop;
    */
}
