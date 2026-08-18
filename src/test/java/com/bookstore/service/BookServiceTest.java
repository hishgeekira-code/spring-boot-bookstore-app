package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    Book book1;
    Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert C. Martin");
        book1.setIsbn("9780132350884");
        book1.setCategory("Technology");
        book1.setPrice(new BigDecimal("38.95"));
        book1.setStock(15);
        book1.setPublicationYear(2008);

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Great Gatsby");
        book2.setAuthor("F. Scott Fitzgerald");
        book2.setIsbn("9780743273565");
        book2.setCategory("Fiction");
        book2.setPrice(new BigDecimal("19.99"));
        book2.setStock(12);
        book2.setPublicationYear(1925);
    }

    @Test
    void search_withNoQueryOrCategory_returnsAllPaged() {
        Page<Book> page = new PageImpl<>(List.of(book1, book2), PageRequest.of(0, 8), 2);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Book> result = bookService.search(null, null, PageRequest.of(0, 8));

        assertThat(result.getContent()).containsExactly(book1, book2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void search_withQuery_callsSearch() {
        Page<Book> page = new PageImpl<>(List.of(book1), PageRequest.of(0, 8), 1);
        when(bookRepository.search(eq("clean"), any(Pageable.class))).thenReturn(page);

        Page<Book> result = bookService.search("clean", null, PageRequest.of(0, 8));

        assertThat(result.getContent()).containsExactly(book1);
        verify(bookRepository).search(eq("clean"), any(Pageable.class));
    }

    @Test
    void search_withCategory_callsFindByCategory() {
        Page<Book> page = new PageImpl<>(List.of(book2), PageRequest.of(0, 8), 1);
        when(bookRepository.findByCategoryIgnoreCase(eq("Fiction"), any(Pageable.class))).thenReturn(page);

        Page<Book> result = bookService.search(null, "Fiction", PageRequest.of(0, 8));

        assertThat(result.getContent()).containsExactly(book2);
        verify(bookRepository).findByCategoryIgnoreCase(eq("Fiction"), any(Pageable.class));
    }

    @Test
    void search_withBothQueryAndCategory_callsSearchInCategory() {
        Page<Book> page = new PageImpl<>(List.of(book2), PageRequest.of(0, 8), 1);
        when(bookRepository.searchInCategory(eq("Fiction"), eq("gatsby"), any(Pageable.class))).thenReturn(page);

        Page<Book> result = bookService.search("gatsby", "Fiction", PageRequest.of(0, 8));

        assertThat(result.getContent()).containsExactly(book2);
        verify(bookRepository).searchInCategory(eq("Fiction"), eq("gatsby"), any(Pageable.class));
    }

    @Test
    void getById_whenExists_returnsBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Book found = bookService.getById(1L);

        assertThat(found).isSameAs(book1);
    }

    @Test
    void getById_whenNotFound_throwsNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.NOT_FOUND);
    }

    @Test
    void create_whenIsbnUnique_savesAndReturnsBook() {
        when(bookRepository.existsByIsbn("9780132350884")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Book created = bookService.create(book1);

        assertThat(created.getIsbn()).isEqualTo("9780132350884");
        verify(bookRepository).save(book1);
    }

    @Test
    void create_whenIsbnDuplicate_throwsConflict() {
        when(bookRepository.existsByIsbn("9780132350884")).thenReturn(true);

        assertThatThrownBy(() -> bookService.create(book1))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    void update_whenIsbnUnique_updatesAndReturns() {
        when(bookRepository.existsByIsbnAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Book updated = bookService.update(1L, book2);

        assertThat(updated.getTitle()).isEqualTo("The Great Gatsby");
        assertThat(updated.getIsbn()).isEqualTo(book2.getIsbn());
    }

    @Test
    void update_whenIsbnConflict_throwsConflict() {
        when(bookRepository.existsByIsbnAndIdNot(anyString(), anyLong())).thenReturn(true);

        assertThatThrownBy(() -> bookService.update(1L, book2))
                .isInstanceOf(ResponseStatusException.class)
                .hasFieldOrPropertyWithValue("status", HttpStatus.CONFLICT);
    }

    @Test
    void delete_deletesBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        bookService.delete(1L);

        verify(bookRepository).delete(book1);
    }

    @Test
    void listCategories_returnsUnionOfDefaultsAndDb() {
        when(bookRepository.findDistinctCategories()).thenReturn(List.of("Biography", "Fiction"));

        List<String> cats = bookService.listCategories();

        assertThat(cats).containsAll(List.of("Fiction", "Non-Fiction", "Science", "Technology", "History", "Children", "Mystery", "Romance", "Biography"));
        assertThat(cats).doesNotHaveDuplicates();
    }
}