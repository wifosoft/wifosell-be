package com.wifosell.zeus.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.constant.lucence.LuceneAnalysisName;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import lombok.*;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
public class Customer extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField(analyzer = LuceneAnalysisName.VIE_NGRAM, searchAnalyzer = StandardTokenizerFactory.NAME)
    private String fullName;

    private Date dob;   // Date of birth

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @GenericField
    private String e;

    @GenericField
    private String email;

    @GenericField
    private String cin; // Citizen Identification Number

    private String nation;

    private String city;

    private String district;

    private String ward;

    private String addressDetail;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    @IndexedEmbedded
    private User generalManager;

    public enum Sex {
        MALE,
        FEMALE,
        OTHER
    }
}
