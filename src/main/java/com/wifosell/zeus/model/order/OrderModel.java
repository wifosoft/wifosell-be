package com.wifosell.zeus.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.SaleChannelShopRelation;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;

    @JsonIgnore
    public BigDecimal calcSubTotal() {
        BigDecimal subtotal = new BigDecimal(0);
        for (OrderItem orderItem : this.getOrderItems()) {
            subtotal = subtotal.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        }
        return subtotal;
    }
}
