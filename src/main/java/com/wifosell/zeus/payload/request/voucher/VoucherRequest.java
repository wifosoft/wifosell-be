package com.wifosell.zeus.payload.request.voucher;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class VoucherRequest {
    private Long id;

    private Long type;
    private String value;

    private Instant validFrom;

    private Instant validTo;

    private boolean isActivated;
    private String rule;

    private boolean forOldCustomer = false;
    private int forType = 0;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    private String description;

    private Boolean isActive;
}
