package com.wifosell.zeus.model.stock;

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
public class ImportStockTransactionItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Variant variant;

    private Integer quantity;

    private BigDecimal unitCost;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private ImportStockTransaction transaction;
}
