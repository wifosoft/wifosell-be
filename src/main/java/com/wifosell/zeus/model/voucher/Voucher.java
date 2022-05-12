package com.wifosell.zeus.model.voucher;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.VoucherSaleChannelShopRelation;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Voucher extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private int type;
    private String value;

    @JsonFormat(pattern="MM-dd-yyyy")
    private Date validFrom;
    @JsonFormat(pattern="MM-dd-yyyy")
    private Date validTo;

    private boolean isActivated;
    private String rule;

    private boolean forOldCustomer = false;
    private int forType = 0;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_channel_id", referencedColumnName = "id")
    private SaleChannel saleChannel;

    @JsonIgnore
    @OneToMany(mappedBy = "voucher", fetch = FetchType.LAZY)
    //@JsonManagedReference
    List<VoucherSaleChannelShopRelation> voucherSaleChannelShopRelations;
}
