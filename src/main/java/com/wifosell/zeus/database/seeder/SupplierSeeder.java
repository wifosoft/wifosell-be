package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.supplier.AddSupplierRequest;
import com.wifosell.zeus.payload.request.supplier.ISupplierRequest;
import com.wifosell.zeus.repository.SupplierRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.utils.FileUtils;
import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class SupplierSeeder extends BaseSeeder implements ISeeder {
    SupplierRepository supplierRepository;
    UserRepository userRepository;

    @Override
    public void prepareJpaRepository() {
        supplierRepository = this.factory.getRepository(SupplierRepository.class);
        userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/supplier.json");
            AddSupplierRequest[] requests = new ObjectMapper().readValue(file, AddSupplierRequest[].class);
            file.close();
            for (AddSupplierRequest request : requests) {
                this.addSupplierByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSupplierByRequest(@NonNull ISupplierRequest request, @NonNull User gm) {
        Supplier supplier = new Supplier();
        Optional.ofNullable(request.getName()).ifPresent(supplier::setName);
        Optional.ofNullable(request.getPhone()).ifPresent(supplier::setPhone);
        Optional.ofNullable(request.getEmail()).ifPresent(supplier::setEmail);
        Optional.ofNullable(request.getNation()).ifPresent(supplier::setNation);
        Optional.ofNullable(request.getCity()).ifPresent(supplier::setCity);
        Optional.ofNullable(request.getDistrict()).ifPresent(supplier::setDistrict);
        Optional.ofNullable(request.getWard()).ifPresent(supplier::setWard);
        Optional.ofNullable(request.getAddressDetail()).ifPresent(supplier::setAddressDetail);
        Optional.ofNullable(request.getIsActive()).ifPresent(supplier::setIsActive);
        supplier.setGeneralManager(gm);
        supplierRepository.save(supplier);
    }
}
