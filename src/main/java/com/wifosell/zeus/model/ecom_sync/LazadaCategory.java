package com.wifosell.zeus.model.ecom_sync;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wifosell.zeus.model.audit.BasicEntity;
import javassist.compiler.ast.CastExpr;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "lazada_categories")
public class LazadaCategory extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lazadaCategoryId;

    private boolean var;
    private String name;
    private boolean leaf;

    private Long parent_category_id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    LazadaCategory parentCategoryId;

    @OneToMany(mappedBy =  "parentCategoryId" ,cascade = CascadeType.ALL)
    private List<LazadaCategory> categoryChildren;

    @OneToMany(mappedBy = "sysLazadaCategoryId" ,cascade = CascadeType.ALL)
    private List<LazadaCategoryAttribute> listCategoryAttribute;
}
