package com.wifosell.zeus.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 255)
    private String fullname;

    @Size(max = 50)
    private String phone;

    @Size(max = 50)
    private Date birthday;

    @Size(max = 50)
    private String email;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String ward;

    @Size(max = 50)
    private String district;

    @Size(max = 50)
    private String facebook;

    private int sex;

    @Column(name = "support_staff_id")
    private Long supportStaffID;

    @Size(max = 255)
    @Column(name = "address_detail")
    private String address;

    @Size(max = 255)
    private String cmnd;

    @Column(name = "customer_type")
    private int customerType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;
}
