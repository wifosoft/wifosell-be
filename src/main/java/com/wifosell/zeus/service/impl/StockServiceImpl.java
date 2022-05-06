package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.StockService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("StockService")
@Transactional
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final VariantRepository variantRepository;

    @Override
    public void importStocks(@NonNull Long userId, ImportStocksRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseRepository.getByIdWithGm(gm.getId(), request.getWarehouseId());
        Supplier supplier = supplierRepository.getByIdWithGm(gm.getId(), request.getSupplierId());
        request.getItems().forEach(item -> {
            Variant variant = variantRepository.getById(item.getVariantId());
            Stock stock = stockRepository.getStockByWarehouseIdAndVariantId(warehouse.getId(), variant.getId());
            if (stock != null) {
                stock.setActualQuantity(stock.getActualQuantity() + item.getQuantity());
                stock.setQuantity(stock.getQuantity() + item.getQuantity());
            } else {
                stock = Stock.builder()
                        .warehouse(warehouse)
                        .variant(variant)
                        .actualQuantity(item.getQuantity())
                        .quantity(item.getQuantity())
                        .build();
            }
            stockRepository.save(stock);
        });
        // TODO haukc: add transaction
    }

    @Override
    public void importStocksFromExcel(@NonNull Long userId, ImportStocksFromExcelRequest request) {
        // TODO haukc
    }

    @Override
    public void transferStocks(@NonNull Long userId, TransferStocksRequest request) {
        // TODO haukc
    }
}
