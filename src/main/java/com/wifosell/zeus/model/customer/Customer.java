package com.wifosell.zeus.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import lombok.*;
import org.hibernate.search.annotations.*;

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

    @Field(termVector = TermVector.YES, analyze = Analyze.YES, store = Store.NO)
    private String fullName;

    private Date dob;   // Date of birth

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private String phone;

    private String email;

    private String cin; // Citizen Identification Number

    private String nation;

    @Field(analyze = Analyze.NO)
    @Facet
    private String city;

    private String district;

    private String ward;

    private String addressDetail;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;

    public enum Sex {
        MALE,
        FEMALE,
        OTHER
    }
}
