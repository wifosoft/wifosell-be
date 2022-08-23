package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.stock.ImportStocksRequest;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.StockService;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class ImportStockSeeder extends BaseSeeder implements ISeeder {
    private UserRepository userRepository;
    private StockService stockService;

    @Override
    public void prepareJpaRepository() {
        userRepository = context.getBean(UserRepository.class);
        stockService = context.getBean(StockService.class);
    }

    @Deprecated
    @Override
    public void run() {
        User user = userRepository.getUserByName("manager1");

        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/import_stock.json");
            ImportStocksRequest[] requests = new ObjectMapper().readValue(file, ImportStocksRequest[].class);
            file.close();
            for (ImportStocksRequest request : requests) {
                stockService.importStocks(user.getId(), request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
