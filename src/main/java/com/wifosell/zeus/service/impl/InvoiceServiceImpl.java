package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.invoice.Invoice;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.repository.InvoiceRepository;
import com.wifosell.zeus.repository.OrderRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.InvoiceService;
import com.wifosell.zeus.specs.InvoiceSpecs;
import com.wifosell.zeus.specs.OrderSpecs;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("InvoiceService")
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public Invoice getInvoice(Long userId, @NonNull Long invoiceId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return invoiceRepository.getOne(
                InvoiceSpecs.hasGeneralManager(gmId)
                        .and(InvoiceSpecs.hasId(invoiceId))
        );
    }

    @Override
    public Invoice createInvoiceByOrderId(Long userId, @NonNull Long orderId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        OrderModel order = orderRepository.getOne(
                OrderSpecs.hasGeneralManager(gmId)
                        .and(OrderSpecs.hasId(orderId))
        );
        return this.createInvoiceByOrder(userId, order);
    }

    @Override
    public Invoice createInvoiceByOrder(Long userId, @NonNull OrderModel order) {
        if (order.getInvoice() != null) {
            throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.INVOICE_EXISTING));
        }

        Invoice invoice = new Invoice();

        return invoiceRepository.save(invoice);
    }
}
