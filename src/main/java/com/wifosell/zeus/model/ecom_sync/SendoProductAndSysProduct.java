package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "sendo_product_and_sys_product",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sendo_product_id", "sys_product_id", "general_manager_id"})
)
public class SendoProductAndSysProduct extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sendo_product_id", referencedColumnName = "id")
    SendoProduct sendoProduct;

    @OneToOne
    @JoinColumn(name = "sys_product_id", referencedColumnName = "id")
    Product sysProduct;

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
