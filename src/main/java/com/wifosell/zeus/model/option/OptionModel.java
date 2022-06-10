package com.wifosell.zeus.model.option;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.user.User;
import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.*;
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
public class OptionModel extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField(analyzer = "vieNGram", searchAnalyzer = "standard")
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "option", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OptionValue> optionValues = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private Product product;

    @IndexedEmbedded
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
