package com.github.blquinn.minimalapi.common.jpa;

import static java.lang.Math.min;

import com.github.blquinn.minimalapi.common.pagination.Page;
import com.github.blquinn.minimalapi.common.pagination.PageRequest;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public record DbHandle(EntityManager manager) {
  public <T> T withTransaction(Function<EntityTransaction, T> callback) {
    var tx = manager.getTransaction();
    tx.begin();
    try {
      var ret = callback.apply(tx);
        if (tx.isActive()) {
            tx.commit();
        }
      return ret;
    } catch (Throwable t) {
        if (tx.isActive()) {
            tx.rollback();
        }
      throw t;
    }
  }

  public JPAQuery<Void> query() {
    return new JPAQuery<>(manager);
  }


  public <ENTITY, OUT> Page<OUT> pageQuery(PageRequest pr,
                                           Function<JPAQuery<Void>, JPAQuery<ENTITY>> cb,
                                           Function<ENTITY, OUT> mapper) {
    var query = cb.apply(query());
    query.limit(pr.getPageSize() + 1)
        .offset((long) (pr.getPage() - 1) * pr.getPageSize());
    var results = query.fetch();
    Integer nextPage = null;
    if (results.size() > pr.getPageSize()) {
      nextPage = pr.getPage() + 1;
    }

    var content = results.subList(0, min(pr.getPageSize(), results.size()))
        .stream().map(mapper).toList();

    return new Page<>(nextPage, content);
  }

  public <T> Page<T> pageQuery(PageRequest pr, Function<JPAQuery<Void>, JPAQuery<T>> cb) {
    return pageQuery(pr, cb, Function.identity());
  }
}
