package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.stock.*;
import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.stock.ImportStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksFromExcelRequest;
import com.wifosell.zeus.payload.request.stock.TransferStocksRequest;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.service.StockService;
import com.wifosell.zeus.service.SupplierService;
import com.wifosell.zeus.service.VariantService;
import com.wifosell.zeus.service.WarehouseService;
import com.wifosell.zeus.service.impl.batch_process.warehouse.ImportStockRow;
import com.wifosell.zeus.service.impl.batch_process.warehouse.ImportStockServiceImpl;
import com.wifosell.zeus.service.impl.batch_process.warehouse.TransferStockRow;
import com.wifosell.zeus.service.impl.batch_process.warehouse.TransferStockServiceImpl;
import com.wifosell.zeus.service.impl.storage.FileSystemStorageService;
import com.wifosell.zeus.specs.ImportStockTransactionSpecs;
import com.wifosell.zeus.specs.TransferStockTransactionSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final VariantRepository variantRepository;
    private final ImportStockTransactionRepository importStockTransactionRepository;
    private final ImportStockTransactionItemRepository importStockTransactionItemRepository;
    private final TransferStockTransactionRepository transferStockTransactionRepository;
    private final TransferStockTransactionItemRepository transferStockTransactionItemRepository;
    private final WarehouseService warehouseService;
    private final SupplierService supplierService;
    private final VariantService variantService;

    private final JobScheduler jobScheduler;
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private final FileSystemStorageService fileStorageService;

    @Override
    public ImportStockTransaction importStocks(Long userId, ImportStocksRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseService.getWarehouse(userId, request.getWarehouseId());
        Supplier supplier = supplierService.getSupplier(userId, request.getSupplierId());

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
            Variant variant = variantService.getVariant(userId, item.getVariantId());
            importStock(warehouse, variant, item.getQuantity(), item.getQuantity());

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
    public void importStock(Warehouse warehouse, Variant variant, Integer actualQuantity, Integer quantity) {
        Stock stock = stockRepository.getStockByWarehouseIdAndVariantId(warehouse.getId(), variant.getId());
        if (stock != null) {
            stock.setActualQuantity(stock.getActualQuantity() + actualQuantity);
            stock.setQuantity(stock.getQuantity() + quantity);
        } else {
            stock = Stock.builder()
                    .warehouse(warehouse)
                    .variant(variant)
                    .actualQuantity(actualQuantity)
                    .quantity(quantity)
                    .build();
        }
        stockRepository.save(stock);
    }

    @Override
    public void updateStock(Warehouse warehouse, Variant variant, Integer actualQuantity, Integer quantity) {
        Stock stock = stockRepository.getStockByWarehouseIdAndVariantId(warehouse.getId(), variant.getId());
        if (stock != null) {
            stock.setActualQuantity(actualQuantity);
            stock.setQuantity(quantity);
        } else {
            stock = Stock.builder()
                    .warehouse(warehouse)
                    .variant(variant)
                    .actualQuantity(actualQuantity)
                    .quantity(quantity)
                    .build();
        }
        stockRepository.save(stock);
    }

    @Override
    public ImportStockTransaction createImportStockTransactionExcel(Long userId, ImportStocksFromExcelRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = warehouseService.getWarehouse(userId, request.getWarehouseId());
        Supplier supplier = supplierService.getSupplier(userId, request.getSupplierId());

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
        logger.info("Job scheduled import stocks transaction : " + scheduledJobIdEmail);

        return transaction;
    }

    @Override
    public void importStocksFromExcel(Long userId, Long transactionId) {
        ImportStockTransaction importStockTransaction = importStockTransactionRepository.getById(transactionId);
        importStockTransaction.setProcessingStatus(ImportStockTransaction.PROCESSING_STATUS.PROCESSING);
        importStockTransaction.setProcessingNote("Đang thực thi xử lý");
        importStockTransactionRepository.save(importStockTransaction);

        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse warehouse = importStockTransaction.getWarehouse();

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
                Variant variant = variantRepository.getBySKUNoThrow(item.getSku(), gm.getId());
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
    public ImportStockTransaction getImportStockTransaction(Long userId, Long importStockTransactionId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return importStockTransactionRepository.getOne(
                ImportStockTransactionSpecs.hasGeneralManager(gmId)
                        .and(ImportStockTransactionSpecs.hasId(importStockTransactionId))
        );
    }

    @Override
    public TransferStockTransaction transferStocks(Long userId, TransferStocksRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse fromWarehouse = warehouseService.getWarehouse(userId, request.getFromWarehouseId());
        Warehouse toWarehouse = warehouseService.getWarehouse(userId, request.getToWarehouseId());

        List<TransferStockTransactionItem> transactionItems = new ArrayList<>();
        TransferStockTransaction transaction = TransferStockTransaction.builder()
                .fromWarehouse(fromWarehouse)
                .toWarehouse(toWarehouse)
                .type(TransferStockTransaction.TYPE.MANUAL)
                .source("")
                .processingStatus(TransferStockTransaction.PROCESSING_STATUS.PROCESSED)
                .processingNote("")
                .generalManager(gm)
                .build();

        request.getItems().forEach(item -> {
            Variant variant = variantService.getVariant(userId, item.getVariantId());

            // From
            Stock fromStock = stockRepository.getStockByWarehouseIdAndVariantId(fromWarehouse.getId(), variant.getId());

            if (fromStock == null)
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.STOCK_NOT_FOUND));

            if (fromStock.getQuantity() < item.getQuantity())
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.STOCK_QUANTITY_NOT_ENOUGH));

            if (fromStock.getActualQuantity() < item.getQuantity())
                throw new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.STOCK_ACTUAL_QUANTITY_NOT_ENOUGH));

            fromStock.setActualQuantity(fromStock.getActualQuantity() - item.getQuantity());
            fromStock.setQuantity(fromStock.getQuantity() - item.getQuantity());

            stockRepository.save(fromStock);

            // To
            Stock toStock = stockRepository.getStockByWarehouseIdAndVariantId(toWarehouse.getId(), variant.getId());

            if (toStock != null) {
                toStock.setActualQuantity(toStock.getActualQuantity() + item.getQuantity());
                toStock.setQuantity(toStock.getQuantity() + item.getQuantity());
            } else {
                toStock = Stock.builder()
                        .warehouse(toWarehouse)
                        .variant(variant)
                        .actualQuantity(item.getQuantity())
                        .quantity(item.getQuantity())
                        .build();
            }

            stockRepository.save(toStock);

            // Transaction
            TransferStockTransactionItem transactionItem = TransferStockTransactionItem.builder()
                    .variant(variant)
                    .quantity(item.getQuantity())
                    .transaction(transaction)
                    .build();
            transactionItems.add(transactionItem);
        });

        transaction.setItems(transferStockTransactionItemRepository.saveAll(transactionItems));
        transferStockTransactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public TransferStockTransaction createTransferStockTransactionExcel(Long userId, TransferStocksFromExcelRequest request) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse fromWarehouse = warehouseService.getWarehouse(userId, request.getFromWarehouseId());
        Warehouse toWarehouse = warehouseService.getWarehouse(userId, request.getToWarehouseId());

        TransferStockTransaction transaction = TransferStockTransaction.builder()
                .fromWarehouse(fromWarehouse)
                .toWarehouse(toWarehouse)
                .type(TransferStockTransaction.TYPE.EXCEL)
                .source(request.getSource())
                .processingStatus(TransferStockTransaction.PROCESSING_STATUS.QUEUED)
                .processingNote("Tạo phiếu chuyển kho thành công. File sẽ được xử lý trong hàng đợi.")
                .generalManager(gm)
                .build();
        transferStockTransactionRepository.save(transaction);

        JobId scheduledJobIdEmail = jobScheduler.schedule(
                Instant.now().plusSeconds(5),
                () -> transferStocksFromExcel(userId, transaction.getId())
        );
        logger.info("Job scheduled transfer stocks transaction : " + scheduledJobIdEmail);

        return transaction;
    }

    @Override
    public void transferStocksFromExcel(Long userId, Long transactionId) {
        TransferStockTransaction transaction = transferStockTransactionRepository.getById(transactionId);
        transaction.setProcessingStatus(TransferStockTransaction.PROCESSING_STATUS.PROCESSING);
        transaction.setProcessingNote("Đang thực thi xử lý.");
        transferStockTransactionRepository.save(transaction);

        User gm = userRepository.getUserById(userId).getGeneralManager();
        Warehouse fromWarehouse = transaction.getFromWarehouse();
        Warehouse toWarehouse = transaction.getToWarehouse();

        String filePathImportWarehouseStock = transaction.getSource();
        List<TransferStockTransactionItem> transactionItems = new ArrayList<>();

        Resource resource = fileStorageService.loadFileAsResource(filePathImportWarehouseStock);
        TransferStockServiceImpl transferStockService = new TransferStockServiceImpl();
        int successRecord = 0;
        int errorRecord = 0;
        try {
            File f = new File(resource.getURI());
            List<TransferStockRow> listRow = transferStockService.transferStocks(f);
            for (TransferStockRow item : listRow) {
                Variant variant = variantRepository.getBySKUNoThrow(item.getSku(), gm.getId());
                if (variant == null) {
                    errorRecord += 1;
                    continue;
                }

                // From
                Stock fromStock = stockRepository.getStockByWarehouseIdAndVariantId(fromWarehouse.getId(), variant.getId());

                if (fromStock == null || fromStock.getQuantity() < item.getQuantity() || fromStock.getActualQuantity() < item.getQuantity()) {
                    errorRecord += 1;
                    continue;
                }

                fromStock.setActualQuantity(fromStock.getActualQuantity() - item.getQuantity());
                fromStock.setQuantity(fromStock.getQuantity() - item.getQuantity());

                stockRepository.save(fromStock);

                // To
                Stock toStock = stockRepository.getStockByWarehouseIdAndVariantId(toWarehouse.getId(), variant.getId());

                if (toStock != null) {
                    toStock.setActualQuantity(toStock.getActualQuantity() + item.getQuantity());
                    toStock.setQuantity(toStock.getQuantity() + item.getQuantity());
                } else {
                    toStock = Stock.builder()
                            .warehouse(toWarehouse)
                            .variant(variant)
                            .actualQuantity(item.getQuantity())
                            .quantity(item.getQuantity())
                            .build();
                }

                stockRepository.save(toStock);

                // Transaction
                TransferStockTransactionItem transactionItem = TransferStockTransactionItem.builder()
                        .variant(variant)
                        .quantity(item.getQuantity())
                        .transaction(transaction)
                        .build();
                transactionItems.add(transactionItem);

                transaction.setItems(transferStockTransactionItemRepository.saveAll(transactionItems));
                successRecord += 1;
            }

            transaction.setProcessingStatus(TransferStockTransaction.PROCESSING_STATUS.PROCESSED);
            transaction.setProcessingNote(String.format("Đã xử lý, thành công %d sản phẩm - không thành công %d sản phẩm.", successRecord, errorRecord));
            transferStockTransactionRepository.save(transaction);
        } catch (InvalidFormatException | IOException e) {
            e.printStackTrace();
            transaction.setProcessingStatus(TransferStockTransaction.PROCESSING_STATUS.PROCESSED);
            transaction.setProcessingNote("Có lỗi xảy ra trong quá trình xử lý");
            transferStockTransactionRepository.save(transaction);
        }
    }

    @Override
    public Page<TransferStockTransaction> getTransferStockTransactions(Long userId, List<TransferStockTransaction.TYPE> types, List<TransferStockTransaction.PROCESSING_STATUS> statuses, List<Boolean> isActives, Integer offset, Integer limit, String sortBy, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return transferStockTransactionRepository.findAll(
                TransferStockTransactionSpecs.hasGeneralManager(gmId)
                        .and(TransferStockTransactionSpecs.inTypes(types))
                        .and(TransferStockTransactionSpecs.inProcessingStatuses(statuses))
                        .and(TransferStockTransactionSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public TransferStockTransaction getTransferStockTransaction(Long userId, Long transferStockTransactionId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return transferStockTransactionRepository.getOne(
                TransferStockTransactionSpecs.hasGeneralManager(gmId)
                        .and(TransferStockTransactionSpecs.hasId(transferStockTransactionId))
        );
    }
}
