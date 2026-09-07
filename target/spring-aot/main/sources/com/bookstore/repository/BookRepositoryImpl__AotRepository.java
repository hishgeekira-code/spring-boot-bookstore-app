package com.bookstore.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Long;
import java.lang.String;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link BookRepository}.
 */
@Generated
public class BookRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public BookRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link BookRepository#existsByIsbn(java.lang.String)}.
   */
  public boolean existsByIsbn(String isbn) {
    String queryString = "SELECT b.id FROM Book b WHERE b.isbn = :isbn";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("isbn", isbn);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }

  /**
   * AOT generated implementation of {@link BookRepository#existsByIsbnAndIdNot(java.lang.String,java.lang.Long)}.
   */
  public boolean existsByIsbnAndIdNot(String isbn, Long id) {
    String queryString = "SELECT b.id FROM Book b WHERE b.isbn = :isbn AND b.id != :id";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("isbn", isbn);
    query.setParameter("id", id);
    query.setMaxResults(1);

    return !query.getResultList().isEmpty();
  }
}
