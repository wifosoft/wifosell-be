package com.wifosell.zeus.database;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

public abstract class BaseSeeder implements ISeeder{
    public EntityManager entityManager;
    public RepositoryFactorySupport factory;
    public void InjectEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.factory = new JpaRepositoryFactory(entityManager);
    }
}
