package com.wifosell.zeus.payload.response.stock;

import com.wifosell.zeus.model.stock.TransferStockTransaction;
import com.wifosell.zeus.model.stock.TransferStockTransactionItem;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.product.VariantResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TransferStockTransactionResponse extends BasicEntityResponse {
    private final Warehouse fromWarehouse;
    private final Warehouse toWarehouse;
    private final TransferStockTransaction.TYPE type;
    private final TransferStockTransaction.PROCESSING_STATUS processingStatus;
    private final String processingNote;
    private final List<TransferStockTransactionItemResponse> items;

    public TransferStockTransactionResponse(TransferStockTransaction transaction) {
        super(transaction);
        this.fromWarehouse = transaction.getFromWarehouse();
        this.toWarehouse = transaction.getToWarehouse();
        this.type = transaction.getType();
        this.processingStatus = transaction.getProcessingStatus();
        this.processingNote = transaction.getProcessingNote();
        this.items = transaction.getItems().stream().map(TransferStockTransactionItemResponse::new).collect(Collectors.toList());
    }

    @Getter
    private static class TransferStockTransactionItemResponse extends BasicEntityResponse {
        private final VariantResponse variant;
        private final Integer quantity;

        public TransferStockTransactionItemResponse(TransferStockTransactionItem item) {
            super(item);
            this.variant = new VariantResponse(item.getVariant());
            this.quantity = item.getQuantity();
        }
    }
}
