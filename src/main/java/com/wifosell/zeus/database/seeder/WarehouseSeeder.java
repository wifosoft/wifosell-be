package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.request.warehouse.WarehouseRequest;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.WarehouseRepository;
import com.wifosell.zeus.utils.FileUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class WarehouseSeeder extends BaseSeeder implements ISeeder {
    private WarehouseRepository warehouseRepository;
    private UserRepository userRepository;

    @Override
    public void prepareJpaRepository() {
        warehouseRepository = factory.getRepository(WarehouseRepository.class);
        userRepository = factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        ObjectMapper mapper = new ObjectMapper();
        //File file = new File("src/main/java/com/wifosell/zeus/database/data/warehouse.json");
        InputStream file = (new FileUtils()).getFileAsIOStream("data/warehouse.json");

        try {
            WarehouseRequest[] requests = mapper.readValue(file, WarehouseRequest[].class);
            for (WarehouseRequest request : requests) {
                this.addWarehouseByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWarehouseByRequest(@NonNull WarehouseRequest request, @NonNull User gm) {
        Warehouse warehouse = new Warehouse();
        Optional.ofNullable(request.getName()).ifPresent(warehouse::setName);
        Optional.ofNullable(request.getShortName()).ifPresent(warehouse::setShortName);
        Optional.ofNullable(request.getAddress()).ifPresent(warehouse::setAddress);
        Optional.ofNullable(request.getPhone()).ifPresent(warehouse::setPhone);
        Optional.ofNullable(request.getDescription()).ifPresent(warehouse::setDescription);
        Optional.ofNullable(request.getIsActive()).ifPresent(warehouse::setIsActive);
        warehouse.setGeneralManager(gm);
        warehouseRepository.save(warehouse);
    }
}
