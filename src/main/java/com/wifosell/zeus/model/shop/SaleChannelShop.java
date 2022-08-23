package com.wifosell.zeus.model.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.warehouse.Warehouse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "sale_channel_id"}))
public class SaleChannelShop {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Shop shop;

    @ManyToOne
    private SaleChannel saleChannel;

    @ManyToOne
    private Warehouse warehouse;


    @OneToMany(mappedBy = "saleChannelShop", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<LazadaSwwAndEcomAccount> lazadaSwwAndEcomAccount = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    @JsonIgnore
    @OneToMany(mappedBy = "saleChannelShop" ,cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<OrderModel> listOrders = new ArrayList<>();


    public List<Long> getAllLinkedSwwId() {
        if (lazadaSwwAndEcomAccount == null) {
            return new ArrayList<>();
        }
        return lazadaSwwAndEcomAccount.stream().map(e -> e.getId()).collect(Collectors.toList());
    }

    public String getHashMergeId() {
        return String.format("%s_%s_%s", shop.getId().toString(), saleChannel.getId().toString(), warehouse.getId().toString());
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
