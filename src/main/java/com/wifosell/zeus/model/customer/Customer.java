package com.wifosell.zeus.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String fullName;

    private Date dob;   // Date of birth

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private String phone;

    private String email;

    private String cin; // Citizen Identification Number

    private String nation;

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
