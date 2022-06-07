package com.wifosell.zeus.payload.response.statistic;

import com.wifosell.zeus.model.user.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TopRevenueEmployeeResponse {
    private User user;
    private BigDecimal total;
    public TopRevenueEmployeeResponse (User user, BigDecimal total) {
        this.user = user;
        this.total = total;
    }
}
