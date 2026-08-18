package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
            select b from Book b
            where lower(b.title) like lower(concat('%', :q, '%'))
               or lower(b.author) like lower(concat('%', :q, '%'))
            """)
    Page<Book> search(@Param("q") String query, Pageable pageable);

    Page<Book> findByCategoryIgnoreCase(String category, Pageable pageable);

    @Query("""
            select b from Book b
            where b.category = :category
              and (lower(b.title) like lower(concat('%', :q, '%'))
                   or lower(b.author) like lower(concat('%', :q, '%')))
            """)
    Page<Book> searchInCategory(@Param("category") String category, @Param("q") String query, Pageable pageable);

    @Query("select distinct b.category from Book b order by b.category")
    List<String> findDistinctCategories();

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);
}
