package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryAttributePayload;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryAttributeRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "lazada_category_attributes")
@NoArgsConstructor
public class LazadaCategoryAttribute extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition="tinyint(1) default 0")
    private boolean isKeyProp;
    @Column(columnDefinition="tinyint(1) default 0")
    private boolean isSaleProp;

    private String name;

    private String inputType;

    private Long lazadaAttributeId;

    @Column(columnDefinition = "MEDIUMTEXT", name = "options")
    private String options;

    @Column(columnDefinition="tinyint(1) default 0")
    private boolean isMandatory;

    private String attributeType;

    private String label;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL , mappedBy = "lazadaCategoryAttribute")
    List<LazadaCategoryAndAttribute> lazadaCategoryAndAttributes;

    public LazadaCategoryAttribute(ResponseCategoryAttributePayload.CategoryAttributeItem item) {
        this.lazadaAttributeId = item.getId();
        this.isKeyProp =  item.getAdvanced().getIsKeyProp() == 1L;
        this.isSaleProp  = item.getIsSaleProp() == 1L;
        this.name = item.getName();
        this.inputType = item.getInputType();
        this.options =  (new Gson()).toJson(item.getOptions());
        this.isMandatory  =item.getIsMandatory() == 1L;
        this.attributeType = item.getAttributeType();
        this.label = item.getLabel();
    }



}
