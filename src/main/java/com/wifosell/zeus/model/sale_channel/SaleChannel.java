package com.wifosell.zeus.model.sale_channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.shop.SaleChannelShop;
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
public class SaleChannel extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotBlank
    private String name;

    @Size(max = 50)
    private String shortName;

    @Lob
    private String description;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "saleChannel", fetch = FetchType.LAZY)
    List<SaleChannelShop> saleChannelShops = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "saleChannel")
    private List<OrderModel> orders = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
