package com.wifosell.zeus.service.impl.batch_process.warehouse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransferStockServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportStockServiceImpl.class);

    public List<TransferStockRow> transferStocks(File file) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        System.out.println("Retrieving Sheets using Iterator");
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        List<TransferStockRow> listImportRow = new ArrayList<>();
        while (iterator.hasNext()) {

            Row currentRow = iterator.next();
            TransferStockRow transferStockRow = new TransferStockRow();

            Iterator<Cell> cellIterator = currentRow.iterator();
            if (currentRow.getRowNum() == 0) continue;
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                int colIndex = currentCell.getColumnIndex();
                switch (colIndex) {
                    case 0:
                        transferStockRow.setSku(currentCell.getStringCellValue());
                        break;
                    case 1:
                        transferStockRow.setQuantity((int) currentCell.getNumericCellValue());
                        break;
                }
            }
            System.out.println();
            listImportRow.add(transferStockRow);

        }
        return listImportRow;
    }
}
