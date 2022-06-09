package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "lazada_category_attributes")
public class LazadaCategoryAttribute extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lazadaAttributeOptionId;

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean isKeyProp;
    @Column(columnDefinition="tinyint(1) default 1")
    private boolean isSaleProp;

    private String name;

    private String inputType;

    @Column(columnDefinition = "MEDIUMTEXT", name = "options")
    private String options;

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean isMandatory;

    private String attributeType;

    private String label;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sys_lazada_category_id", referencedColumnName = "id")
    LazadaCategory sysLazadaCategoryId;


}
