package com.wifosell.zeus.database;

import com.wifosell.zeus.database.seeder.*;

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
        seederTask.add(CategorySeeder.class);
    }
    public void run() {
        this.call(seederTask);
    }
}
