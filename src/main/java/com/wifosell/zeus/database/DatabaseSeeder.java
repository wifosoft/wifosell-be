package com.wifosell.zeus.database;

import com.wifosell.zeus.database.seeder.*;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder extends Seeder {
    private final List<Class<? extends BaseSeeder>> seederTask = new ArrayList<>();

    public DatabaseSeeder(ApplicationContext context, EntityManager entityManager) {
        super(context, entityManager);
    }

    public void prepare() {
        seederTask.clear();
        seederTask.add(RoleSeeder.class);

        seederTask.add(UserSeeder.class);
        seederTask.add(CategorySeeder.class);
        seederTask.add(VoucherSeeder.class);
        seederTask.add(CustomerSeeder.class);
        seederTask.add(SupplierSeeder.class);
        seederTask.add(WarehouseSeeder.class);
        seederTask.add(SaleChannelSeeder.class);
        seederTask.add(ShopSeeder.class);
        seederTask.add(ProductSeeder.class);
        seederTask.add(ImportStockSeeder.class);
        seederTask.add(OrderSeeder.class);
        seederTask.add(EcomAccountSeeder.class);
        seederTask.add(LazadaCategorySeeder.class);
        seederTask.add(LazadaCategoryAttributeSeeder.class);
        seederTask.add(SendoCategorySeeder.class);
    }

    public void run() {
        this.call(seederTask);
    }
}
