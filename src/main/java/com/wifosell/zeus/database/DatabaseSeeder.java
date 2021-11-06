package com.wifosell.zeus.database;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.wifosell.zeus.database.seeder.RoleSeeder;
import com.wifosell.zeus.database.seeder.ShopSeeder;
import com.wifosell.zeus.database.seeder.UserSeeder;
import com.wifosell.zeus.database.seeder.WarehouseSeeder;
import com.wifosell.zeus.model.warehouse.Warehouse;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder extends Seeder {
    List<Class<? extends BaseSeeder>> seederTask = new ArrayList<>();

    public DatabaseSeeder(EntityManager entityManager) {
        super(entityManager);
    }

    public void prepare(){
        seederTask.clear();
        seederTask.add(RoleSeeder.class);
        seederTask.add(UserSeeder.class);
        seederTask.add(ShopSeeder.class);
        seederTask.add(WarehouseSeeder.class);
    }
    public void run() {
        this.call(seederTask);
    }
}
