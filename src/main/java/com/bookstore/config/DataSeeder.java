package com.bookstore.config;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedBooks(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() > 0) {
                return;
            }
            bookRepository.saveAll(sampleBooks());
        };
    }

    private static List<Book> sampleBooks() {
        return List.of(
                book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", "Fiction",
                        "19.99", 12, 1925,
                        "A portrait of the Jazz Age and the American Dream, set in the summer of 1922 on Long Island.",
                        "https://images.unsplash.com/photo-1544947950-fa07a98d237f"),
                book("1984", "George Orwell", "9780451524935", "Fiction",
                        "14.99", 20, 1949,
                        "Winston Smith wrestles with the all-powerful Party and its thought police in a dystopian society.",
                        "https://images.unsplash.com/photo-1532012197267-da84d127e765"),
                book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "9780062316097", "History",
                        "24.50", 8, 2015,
                        "How a small ape in Africa became the ruler of the planet: a sweeping narrative of human history.",
                        "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c"),
                book("Clean Code", "Robert C. Martin", "9780132350884", "Technology",
                        "38.95", 15, 2008,
                        "A handbook of agile software craftsmanship that every developer should own.",
                        "https://images.unsplash.com/photo-1532012197267-da84d127e765"),
                book("The Silent Patient", "Alex Michaelides", "9781250301697", "Mystery",
                        "16.99", 30, 2019,
                        "Alicia Berenson shoots her husband five times and never speaks again. A therapist is determined to unravel her silence.",
                        "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c"),
                book("A Brief History of Time", "Stephen Hawking", "9780553380163", "Science",
                        "18.75", 10, 1988,
                        "From the Big Bang to black holes, Hawking guides us through the mysteries of the universe.",
                        "https://images.unsplash.com/photo-1553729459-efe14ef6055d"),
                book("The Catcher in the Rye", "J.D. Salinger", "9780316769488", "Fiction",
                        "13.99", 18, 1951,
                        "Holden Caulfield narrates three fateful days in New York after being expelled from prep school.",
                        "https://images.unsplash.com/photo-1544947950-fa07a98d237f"),
                book("Educated", "Tara Westover", "9780399590504", "Non-Fiction",
                        "17.99", 14, 2018,
                        "A memoir about a girl who leaves her survivalist family to pursue a PhD from Cambridge.",
                        "https://images.unsplash.com/photo-1495446815901-a7297e633e8d"),
                book("Cosmos", "Carl Sagan", "9780345539434", "Science",
                        "21.00", 9, 1980,
                        "Sagan's classic journey through the universe, our planet, and our future.",
                        "https://images.unsplash.com/photo-1553729459-efe14ef6055d"),
                book("The Hobbit", "J.R.R. Tolkien", "9780547928227", "Children",
                        "15.49", 25, 1937,
                        "Bilbo Baggins, a comfort-loving hobbit, joins a wizard and dwarves on a dangerous quest.",
                        "https://images.unsplash.com/photo-1544947950-fa07a98d237f"),
                book("Pride and Prejudice", "Jane Austen", "9780141439518", "Romance",
                        "11.99", 16, 1813,
                        "Elizabeth Bennet navigates manners, marriage, and misunderstanding in Regency England.",
                        "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c"),
                book("Steve Jobs", "Walter Isaacson", "9781451648539", "Biography",
                        "29.00", 11, 2011,
                        "The exclusive biography of the brilliant and demanding co-founder of Apple.",
                        "https://images.unsplash.com/photo-1532012197267-da84d127e765")
        );
    }

    private static Book book(String title, String author, String isbn, String category,
                             String price, int stock, int year, String description, String imageUrl) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setCategory(category);
        book.setPrice(new BigDecimal(price));
        book.setStock(stock);
        book.setPublicationYear(year);
        book.setDescription(description);
        book.setImageUrl(imageUrl);
        return book;
    }
}
