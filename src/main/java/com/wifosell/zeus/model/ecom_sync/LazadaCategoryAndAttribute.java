package com.wifosell.zeus.model.ecom_sync;

import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "lazada_caa")
public class LazadaCategoryAndAttribute extends BasicEntity {
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
