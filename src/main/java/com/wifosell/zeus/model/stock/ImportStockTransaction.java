package com.wifosell.zeus.model.stock;

import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.warehouse.Warehouse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportStockTransaction {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Warehouse warehouse;

    @ManyToOne
    private Supplier supplier;

    @OneToMany(mappedBy = "transaction", orphanRemoval = true)
    private List<ImportStockTransactionItem> items = new ArrayList<>();
}
