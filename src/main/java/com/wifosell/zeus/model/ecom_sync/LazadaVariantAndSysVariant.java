package com.wifosell.zeus.model.ecom_sync;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "lazada_variant_and_sys_variant",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lazada_variant_id", "sys_variant_id", "general_manager_id"})
)
public class LazadaVariantAndSysVariant extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lazada_variant_id", referencedColumnName = "id")
    LazadaVariant lazadaVariant;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sys_variant_id", referencedColumnName = "id")
    Variant variant;

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
