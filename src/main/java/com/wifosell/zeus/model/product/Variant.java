package com.wifosell.zeus.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.pricetrack.PriceTrack;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.user.User;
import lombok.*;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Variant extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal originalCost;

    private BigDecimal cost;

    @GenericField
    private String sku;

    private String barcode;

    private int idx = 0;

    @Builder.Default
    @OneToMany(mappedBy = "variant", orphanRemoval = true)
    private List<VariantValue> variantValues = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Product product;

    @Builder.Default
    @OneToMany(mappedBy = "variant")
    @IndexedEmbedded(structure = ObjectStructure.NESTED)
    private List<Stock> stocks = new ArrayList<>();

    @OneToOne(mappedBy = "variant")
    private PriceTrack priceTrack;

    public Integer getStockWarehouse(Long warehouseId) {
        for (var _stock : stocks) {
            if (_stock.getWarehouse().getId().equals(warehouseId)) {
                return _stock.getQuantity();
            }
        }
        return 0;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;

    public List<VariantValue> getVariantValues(boolean available) {
        return variantValues.stream().filter(vv -> vv.isDeleted() != available).collect(Collectors.toList());
    }
}
