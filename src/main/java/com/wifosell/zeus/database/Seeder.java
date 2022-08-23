package com.wifosell.zeus.database;

import com.wifosell.zeus.exception.AppException;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Seeder {
    private final ApplicationContext context;
    private final EntityManager entityManager;

    public Seeder(ApplicationContext context, EntityManager entityManager) {
        this.context = context;
        this.entityManager = entityManager;
    }

    public void call(Class<? extends ISeeder> seeder) {
        if (seeder == null) {
            throw new AppException("Seeder errors");
        }
        try {
            ISeeder instance = (ISeeder) seeder.getDeclaredConstructor().newInstance();
            instance.run();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void call(List<Class<? extends BaseSeeder>> seeders) {
        for (Class<? extends BaseSeeder> seeder : seeders) {
            if (seeder == null) {
                throw new AppException("Seeder errors");
            }

            try {
                BaseSeeder instance = seeder.getDeclaredConstructor().newInstance();
                instance.inject(context, entityManager);
                instance.prepareJpaRepository();
                instance.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
