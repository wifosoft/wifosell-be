package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.payload.provider.lazada.ResponseCategoryTreePayload;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryRepository;
import javassist.compiler.ast.CastExpr;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "lazada_categories")
@NoArgsConstructor
public class LazadaCategory extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lazadaCategoryId;

    private boolean var;
    private String name;
    private boolean leaf;

    //private Long parent_category_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    LazadaCategory parent;

    @OneToMany(mappedBy =  "parent" ,cascade = CascadeType.ALL)
    private List<LazadaCategory> categoryChildren;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lazadaCategory")
    List<LazadaCategoryAndAttribute> lazadaCategoryAndAttributes;

    public LazadaCategory(ResponseCategoryTreePayload.CategoryTreeItem categoryTreeItem){
        this.var = categoryTreeItem.isVar();
        this.name = categoryTreeItem.getName();
        this.leaf  = categoryTreeItem.isLeaf();
        this.lazadaCategoryId = categoryTreeItem.getCategoryId();
    }


    public LazadaCategory(ResponseCategoryTreePayload.CategoryTreeItem categoryTreeItem, LazadaCategory _parent){
        this.var = categoryTreeItem.isVar();
        this.name = categoryTreeItem.getName();
        this.leaf  = categoryTreeItem.isLeaf();
        this.lazadaCategoryId = categoryTreeItem.getCategoryId();
        this.setParent(_parent);
    }
}
