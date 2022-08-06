package com.wifosell.zeus.database;

import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;

public abstract class BaseSeeder implements ISeeder {
    protected ApplicationContext context;
    protected EntityManager entityManager;

    public void inject(ApplicationContext context, EntityManager entityManager) {
        this.context = context;
        this.entityManager = entityManager;
    }
}
