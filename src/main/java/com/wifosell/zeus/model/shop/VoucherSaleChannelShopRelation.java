package com.wifosell.zeus.model.shop;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.voucher.Voucher;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherSaleChannelShopRelation {
    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "sale_channel_id")
    private SaleChannel saleChannel;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
}
