package com.wifosell.zeus.model.ecom_account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcomAccount extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private EcomName ecomName;

    @Column(name = "account_name")
    private String accountName;

    @Column(columnDefinition = "TEXT")
    private String accountInfo; //token , etc ...

    @Column(columnDefinition = "TEXT")
    private String authResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @Column(name = "expiredAt")
    LocalDateTime expiredAt;


    @Column(columnDefinition = "TEXT")
    private String description;

    private String note;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;

    public enum EcomName {
        LAZADA,
        SHOPEE,
        TIKI,
        SENDO
    }

    public enum AccountStatus {
        NONE,
        NO_AUTH,
        AUTH,
        EXPIRED,
        BLOCKED
    }
}
