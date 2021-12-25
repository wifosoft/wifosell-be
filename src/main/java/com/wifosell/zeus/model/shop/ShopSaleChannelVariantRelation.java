package com.wifosell.zeus.model.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.product.Variant;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopSaleChannelVariantRelation {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ShopSaleChannelRelation shopSaleChannelRelation;

    @ManyToOne
    private Variant variant;

    private Long stock = 0L;

    private BigDecimal originalPrice;

    private BigDecimal sellingPrice;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
