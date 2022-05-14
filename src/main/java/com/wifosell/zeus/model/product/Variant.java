package com.wifosell.zeus.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.stock.Stock;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    private BigDecimal cost;

    private String sku;

    //    @Column(unique = true)    // TODO haukc
    private String barcode;

    @OneToMany(mappedBy = "variant", orphanRemoval = true)
    private List<VariantValue> variantValues = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Product product;
    
    @OneToMany(mappedBy = "warehouse")
    private List<Stock> stocks = new ArrayList<>();
}
