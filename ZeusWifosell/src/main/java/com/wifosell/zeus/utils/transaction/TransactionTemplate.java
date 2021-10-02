package com.wifosell.zeus.utils.transaction;

import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class TransactionTemplate {
    @PersistenceContext
    private EntityManager em; //populated in a constructor, for instance

    public void executeInTransaction(Runnable action) {
        try {
            em.getTransaction().begin();
            action.run();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.clear(); // since you're using extended persistence context, you might want this line
        }
    }
}
