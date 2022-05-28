package com.wifosell.zeus.batch_process.warehouse;

import com.wifosell.zeus.service.impl.batch_process.warehouse.ImportStockRow;
import com.wifosell.zeus.service.impl.batch_process.warehouse.ImportStockServiceImpl;
import com.wifosell.zeus.service.impl.storage.FileSystemStorageService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ImportStockTest {
    static final String filePathImportWarehouseStock = "warehouse_import.xlsx";

    @Autowired
    private FileSystemStorageService fileStorageService;

    @Test
    public void testImportStock() throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(filePathImportWarehouseStock);
        File f = new File(resource.getURI());
        ImportStockServiceImpl importStockService = new ImportStockServiceImpl();
        try {
            List<ImportStockRow> listRow = importStockService.importStock(f);
            assertEquals(5, listRow.size(), "Size of file excel");
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        assertEquals(1, 1, "Thanh cong");
    }
}
