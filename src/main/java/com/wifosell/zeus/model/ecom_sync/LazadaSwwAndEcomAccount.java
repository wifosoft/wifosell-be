package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lazada_link_sww_and_ea",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ecom_account_id"}))
public class LazadaSwwAndEcomAccount extends BasicEntity {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "sale_channel_shop_id", referencedColumnName = "id")
    SaleChannelShop saleChannelShop;

    @OneToOne
    @JoinColumn(name = "ecom_account_id", referencedColumnName = "id")
    EcomAccount ecomAccount;
}
