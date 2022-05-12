package com.wifosell.zeus.model.stock;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.warehouse.Warehouse;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "variant_id")
    private Variant variant;

    private Integer actualQuantity; // decrease after the order is paid

    private Integer quantity;   // decrease after the order is created (<= actualQuantity)

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
