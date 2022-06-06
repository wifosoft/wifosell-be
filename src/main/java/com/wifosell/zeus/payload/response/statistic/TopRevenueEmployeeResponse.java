package com.wifosell.zeus.payload.response.statistic;

import com.wifosell.zeus.model.user.User;

import java.math.BigDecimal;

public class TopRevenueEmployeeResponse {
    private User user;
    private BigDecimal total;
    public TopRevenueEmployeeResponse (User user, BigDecimal total) {
        this.user = user;
        this.total = total;
    }
}
