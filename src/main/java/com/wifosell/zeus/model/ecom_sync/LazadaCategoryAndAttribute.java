package com.wifosell.zeus.model.ecom_sync;

import javax.persistence.*;

@Entity
public class LazadaCategoryAndAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sys_lazada_category_id")
    LazadaCategory lazadaCategory;

    @ManyToOne
    @JoinColumn(name = "sys_lazda_category_attribute")
    LazadaCategoryAttribute lazadaCategoryAttribute;
}
