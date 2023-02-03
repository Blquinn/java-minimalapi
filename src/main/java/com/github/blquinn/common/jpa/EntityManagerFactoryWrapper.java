package com.github.blquinn.common.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.function.Function;

public record EntityManagerFactoryWrapper(EntityManagerFactory managerFactory) {

    public <T> T withManager(Function<EntityManager, T> cb) {
        var em = managerFactory.createEntityManager();
        try {
            return cb.apply(em);
        } finally {
            em.close();
        }
    }

    public <T> T withHandle(Function<DbHandle, T> cb) {
        return withManager(em -> cb.apply(new DbHandle(em)));
    }
}
