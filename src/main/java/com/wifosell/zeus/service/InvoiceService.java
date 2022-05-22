package com.wifosell.zeus.service;

import com.wifosell.zeus.model.invoice.Invoice;
import com.wifosell.zeus.model.order.OrderModel;
import lombok.NonNull;

public interface InvoiceService {
    Invoice getInvoice(Long userId, @NonNull Long invoiceId);

    Invoice createInvoiceByOrderId(Long userId, @NonNull Long orderId);

    Invoice createInvoiceByOrder(Long userId, @NonNull OrderModel order);
}
