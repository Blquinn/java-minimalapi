package com.github.blquinn.common.jpa;

import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Function;

public record DbHandle(EntityManager manager) {
    public <T> T withTransaction(Function<EntityTransaction, T> callback) {
        var tx = manager.getTransaction();
        tx.begin();
        try {
            var ret = callback.apply(tx);
            if (tx.isActive()) tx.commit();
            return ret;
        } catch (Throwable t) {
            if (tx.isActive()) tx.rollback();
            throw t;
        }
    }

    public JPAQuery<Void> query() {
        return new JPAQuery<>(manager);
    }
}
