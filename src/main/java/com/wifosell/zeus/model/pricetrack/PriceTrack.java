package com.wifosell.zeus.model.pricetrack;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PriceTrack extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Variant variant;

    private String competitorUrl;

    private BigDecimal competitorPrice;

    private boolean isAutoChangePrice;

    private BigDecimal deltaPrice;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;

    public boolean getIsAutoChangePrice() {
        return isAutoChangePrice;
    }
}
