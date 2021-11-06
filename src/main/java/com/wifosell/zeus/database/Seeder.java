package com.wifosell.zeus.database;

import com.wifosell.zeus.exception.AppException;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Seeder {
    EntityManager entityManager;
    public Seeder(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void call(Class<? extends ISeeder> seeder){
        if(seeder ==null) {
            throw new AppException("Seeder errors");
        }
        try {
            ISeeder instance = (ISeeder) seeder.getDeclaredConstructor().newInstance();
            instance.run();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void call(List<Class<? extends BaseSeeder>> seeders){
        for(Class<? extends  BaseSeeder> seeder : seeders) {
            if (seeder == null) {
                throw new AppException("Seeder errors");
            }

            try {
                BaseSeeder instance = seeder.getDeclaredConstructor().newInstance();
                instance.InjectEntityManager(entityManager);
                instance.prepareJpaRepository();
                instance.run();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
