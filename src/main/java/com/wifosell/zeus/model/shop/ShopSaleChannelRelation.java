package com.wifosell.zeus.model.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSaleChannelRelation {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Shop shop;

    @ManyToOne
    private SaleChannel saleChannel;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
