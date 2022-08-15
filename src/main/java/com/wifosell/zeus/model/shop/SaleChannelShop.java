package com.wifosell.zeus.model.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.ecom_sync.LazadaSwwAndEcomAccount;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "sale_channel_id"}))
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

    @OneToMany(mappedBy = "saleChannelShop")
    protected List<LazadaSwwAndEcomAccount> lazadaSwwAndEcomAccount;

    public List<Long> getAllLinkedSwwId () {
        if(lazadaSwwAndEcomAccount == null){ return new ArrayList<>(); }
        return lazadaSwwAndEcomAccount.stream().map(e -> e.getId()).collect(Collectors.toList());
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
