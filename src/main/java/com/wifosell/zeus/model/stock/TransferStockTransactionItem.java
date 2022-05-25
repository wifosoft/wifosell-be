package com.wifosell.zeus.model.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.product.Variant;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferStockTransactionItem extends BasicEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Variant variant;

    private Integer quantity;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private TransferStockTransaction transaction;
}
