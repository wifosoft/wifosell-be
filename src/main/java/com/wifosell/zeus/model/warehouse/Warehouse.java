package com.wifosell.zeus.model.warehouse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.user.User;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Warehouse extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @GenericField
    private Long id;

    private String name;

    private String shortName;

    private String address;

    private String phone;

    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "warehouse")
    private List<Stock> stocks = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
    private List<SaleChannelShop> saleChannelShops = new ArrayList<>();
}
