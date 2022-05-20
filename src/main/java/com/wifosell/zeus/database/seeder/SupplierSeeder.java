package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.supplier.Supplier;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.SupplierRepository;
import com.wifosell.zeus.repository.UserRepository;

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
        User manager1 =userRepository.getUserByName("manager1");

        Supplier supplier = Supplier.builder().name("Trần Đình Sang")
                .generalManager(manager1)
                .addressDetail("280 Nguyễn Văn Cừ")
                .city("Hồ Chí Minh")
                .district("Quận 5")
                .phone("09999999999")
                .nation("Việt Nam")
                .email("sangtd@gmail.com")
                .ward("P4")
                .build();
        supplierRepository.save(supplier);
    }
}
