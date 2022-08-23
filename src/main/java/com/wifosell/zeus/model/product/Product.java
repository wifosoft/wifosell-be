package com.wifosell.zeus.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.constant.lucence.LuceneAnalysisName;
import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.user.User;
import lombok.*;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Indexed
public class Product extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField(analyzer = LuceneAnalysisName.VIE_NGRAM, searchAnalyzer = StandardTokenizerFactory.NAME)
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private String ProductIdentify;

    private BigDecimal weight;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    @Builder.Default
    private Integer state = 0;

    @Builder.Default
    private Integer status = 0;

    @Builder.Default
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Attribute> attributes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OptionModel> options = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    @IndexedEmbedded
    private List<Variant> variants = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @IndexedEmbedded
    private User generalManager;

    @JsonIgnore
    public List<Attribute> getAttributes(boolean available) {
        return attributes.stream().filter(a -> a.isDeleted() != available).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<ProductImage> getImages(boolean available) {
        return images.stream().filter(i -> i.isDeleted() != available).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<OptionModel> getOptions(boolean available) {
        return options.stream().filter(o -> o.isDeleted() != available).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<Variant> getVariants(boolean available) {
        return variants.stream().filter(v -> v.isDeleted() != available).collect(Collectors.toList());
    }

    @JsonIgnore
    public BigDecimal getWeightLazada() {
        return weight.divide(BigDecimal.valueOf(1000));
    }

    @JsonIgnore
    public static BigDecimal fromWeightLazada(BigDecimal weight) {
        return weight.multiply(BigDecimal.valueOf(1000));
    }
}
