package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stock.ImportStockTransaction;
import com.wifosell.zeus.model.stock.ImportStockTransactionItem;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.StockService;
import com.wifosell.zeus.service.impl.batch_process.warehouse.ImportStockRow;
import com.wifosell.zeus.service.impl.batch_process.warehouse.ImportStockServiceImpl;
import com.wifosell.zeus.service.impl.storage.FileSystemStorageService;
import com.wifosell.zeus.specs.ImportStockTransactionSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service("StockService")
@Transactional
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final VariantRepository variantRepository;
    private final ImportStockTransactionRepository importStockTransactionRepository;
    private final ImportStockTransactionItemRepository importStockTransactionItemRepository;

    private final JobScheduler jobScheduler;
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);


    @Autowired
    private FileSystemStorageService fileStorageService;


    @Override
    public ImportStockTransaction importStocks(@NonNull Long userId, ImportStocksRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseRepository.getByIdWithGm(gm.getId(), request.getWarehouseId());
        Supplier supplier = supplierRepository.getByIdWithGm(gm.getId(), request.getSupplierId());

        List<ImportStockTransactionItem> transactionItems = new ArrayList<>();
        ImportStockTransaction transaction = ImportStockTransaction.builder()
                .warehouse(warehouse)
                .supplier(supplier)
                .type(ImportStockTransaction.TYPE.MANUAL)
                .source("")
                .processingStatus(ImportStockTransaction.PROCESSING_STATUS.PROCESSED)
                .processingNote("")
                .generalManager(gm)
                .build();

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

            ImportStockTransactionItem transactionItem = ImportStockTransactionItem.builder()
                    .variant(variant)
                    .quantity(item.getQuantity())
                    .unitCost(item.getUnitCost())
                    .transaction(transaction)
                    .build();
            transactionItems.add(transactionItem);
        });

        transaction.setItems(importStockTransactionItemRepository.saveAll(transactionItems));
        importStockTransactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public ImportStockTransaction createImportStockTransactionExcel(@NonNull Long userId, ImportStocksFromExcelRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseRepository.getByIdWithGm(gm.getId(), request.getWarehouseId());
        Supplier supplier = supplierRepository.getByIdWithGm(gm.getId(), request.getSupplierId());

        ImportStockTransaction transaction = ImportStockTransaction.builder()
                .warehouse(warehouse)
                .supplier(supplier)
                .type(ImportStockTransaction.TYPE.EXCEL)
                .source(request.getSource())
                .processingStatus(ImportStockTransaction.PROCESSING_STATUS.QUEUED)
                .processingNote("Tạo phiếu nhập kho thành công. File sẽ được xử lý trong hàng đợi")
                .generalManager(gm)
                .build();
        importStockTransactionRepository.save(transaction);

        JobId scheduledJobIdEmail = jobScheduler.schedule(
                Instant.now().plusSeconds(5),
                () -> importStocksFromExcel(userId, transaction.getId())
        );
        logger.info("Job scheduled import stock transaction : " + scheduledJobIdEmail);


        return transaction;
    }

    @Override
    public ImportStockTransaction importStocksFromExcel(@NonNull Long userId, @NonNull Long transactionId) {
        ImportStockTransaction importStockTransaction = importStockTransactionRepository.getById(transactionId);
        importStockTransaction.setProcessingStatus(ImportStockTransaction.PROCESSING_STATUS.PROCESSING);
        importStockTransaction.setProcessingNote("Đang thực thi xử lý");
        importStockTransactionRepository.save(importStockTransaction);

        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = importStockTransaction.getWarehouse();
        Supplier supplier = importStockTransaction.getSupplier();

        String filePathImportWarehouseStock = importStockTransaction.getSource();
        List<ImportStockTransactionItem> transactionItems = new ArrayList<>();

        Resource resource = fileStorageService.loadFileAsResource(filePathImportWarehouseStock);
        ImportStockServiceImpl importStockService = new ImportStockServiceImpl();
        int successRecord = 0;
        int errorRecord = 0;
        try {
            File f = new File(resource.getURI());
            List<ImportStockRow> listRow = importStockService.importStock(f);
            for (ImportStockRow item : listRow) {
                Variant variant = variantRepository.getBySKUNoThrow(item.getSku());
                if (variant == null) {
                    errorRecord += 1;
                    continue;
                }
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

                ImportStockTransactionItem transactionItem = ImportStockTransactionItem.builder()
                        .variant(variant)
                        .quantity(item.getQuantity())
                        .unitCost(item.getUnitCost())
                        .transaction(importStockTransaction)
                        .build();
                transactionItems.add(transactionItem);

                importStockTransaction.setItems(importStockTransactionItemRepository.saveAll(transactionItems));
                successRecord += 1;
            }
            ;
            importStockTransaction.setProcessingStatus(ImportStockTransaction.PROCESSING_STATUS.PROCESSED);
            importStockTransaction.setProcessingNote(String.format("Đã xử lý, thành công %d sản phẩm - không thành công %d sản phẩm.", successRecord, errorRecord));
            importStockTransactionRepository.save(importStockTransaction);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
            importStockTransaction.setProcessingStatus(ImportStockTransaction.PROCESSING_STATUS.PROCESSED);
            importStockTransaction.setProcessingNote("Có lỗi xảy ra trong quá trình xử lý");
            importStockTransactionRepository.save(importStockTransaction);
        }

        return importStockTransaction;
    }

    @Override
    public Page<ImportStockTransaction> getImportStockTransactions(
            Long userId,
            List<ImportStockTransaction.TYPE> types,
            List<ImportStockTransaction.PROCESSING_STATUS> statuses,
            List<Boolean> isActives,
            Integer offset, Integer limit, String sortBy, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return importStockTransactionRepository.findAll(
                ImportStockTransactionSpecs.hasGeneralManager(gmId)
                        .and(ImportStockTransactionSpecs.inTypes(types))
                        .and(ImportStockTransactionSpecs.inProcessingStatuses(statuses))
                        .and(ImportStockTransactionSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public ImportStockTransaction getImportStockTransaction(Long userId, @NonNull Long importStockTransactionId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return importStockTransactionRepository.getOne(
                ImportStockTransactionSpecs.hasGeneralManager(gmId)
                        .and(ImportStockTransactionSpecs.hasId(importStockTransactionId))
        );
    }

    @Override
    public void transferStocks(@NonNull Long userId, TransferStocksRequest request) {
        // TODO haukc
    }
}
