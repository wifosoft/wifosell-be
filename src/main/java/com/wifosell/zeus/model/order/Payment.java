package com.wifosell.zeus.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private METHOD method;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    @Lob
    private String info;

    @JsonIgnore
    @OneToOne(mappedBy = "payment")
    private OrderModel order;

    public enum METHOD {
        COD,
        BANKING,
        E_WALLET
    }

    public enum STATUS {
        UNPAID,
        PAID,
        REFUNDED
    }
}
