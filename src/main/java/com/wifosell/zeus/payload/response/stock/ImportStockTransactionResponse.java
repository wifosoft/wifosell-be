package com.wifosell.zeus.payload.response.stock;

import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.model.stock.ImportStockTransactionItem;
import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.product.VariantResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ImportStockTransactionResponse extends BasicEntityResponse {
    private final Warehouse warehouse;
    private final Supplier supplier;
    private final ImportStockTransaction.TYPE type;
    private final ImportStockTransaction.PROCESSING_STATUS processingStatus;
    private final String processingNote;
    private final List<ImportStockTransactionItemResponse> items;

    public ImportStockTransactionResponse(ImportStockTransaction transaction) {
        super(transaction);
        this.warehouse = transaction.getWarehouse();
        this.supplier = transaction.getSupplier();
        this.type = transaction.getType();
        this.processingStatus = transaction.getProcessingStatus();
        this.processingNote = transaction.getProcessingNote();
        this.items = transaction.getItems().stream().map(ImportStockTransactionItemResponse::new).collect(Collectors.toList());
    }

    @Getter
    private static class ImportStockTransactionItemResponse extends BasicEntityResponse {
        private final VariantResponse variant;
        private final Integer quantity;
        private final BigDecimal unitCost;

        public ImportStockTransactionItemResponse(ImportStockTransactionItem item) {
            super(item);
            this.variant = new VariantResponse(item.getVariant());
            this.quantity = item.getQuantity();
            this.unitCost = item.getUnitCost();
        }
    }
}
