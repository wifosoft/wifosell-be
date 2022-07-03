package com.wifosell.zeus.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrderModel extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "sale_channel_id")
    private SaleChannel saleChannel;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal subtotal;

    private BigDecimal shippingFee;

    @Enumerated(EnumType.STRING)
    private STATUS status;

    @Builder.Default
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<OrderStep> steps = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "is_canceled")
    private boolean isCanceled;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;

    @JsonIgnore
    public BigDecimal calcSubTotal() {
        BigDecimal subtotal = new BigDecimal(0);
        for (OrderItem orderItem : this.getOrderItems()) {
            subtotal = subtotal.add(orderItem.getSubtotal());
        }
        return subtotal;
    }

    public boolean getIsComplete() {
        return this.isComplete;
    }

    public boolean getIsCanceled() {
        return this.isCanceled;
    }

    public enum STATUS {
        CREATED,
        CONFIRMED,
        PACKED,
        SHIPPING,
        SHIPPED,
        COMPLETE,
        CANCELED
    }
}
