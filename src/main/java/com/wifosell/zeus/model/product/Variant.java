package com.wifosell.zeus.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.user.User;
import lombok.*;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

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
@Indexed
public class Variant extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal cost;

    //    @Column(unique = true)
    @GenericField
    private String sku;

    private String barcode;

    @Builder.Default
    @OneToMany(mappedBy = "variant", orphanRemoval = true)
    private List<VariantValue> variantValues = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @IndexedEmbedded
    private Product product;

    @Builder.Default
    @OneToMany(mappedBy = "variant")
    @IndexedEmbedded(structure = ObjectStructure.NESTED)
    private List<Stock> stocks = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @IndexedEmbedded
    private User generalManager;
}
