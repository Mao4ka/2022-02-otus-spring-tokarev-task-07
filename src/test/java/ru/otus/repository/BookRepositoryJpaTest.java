package ru.otus.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий Book на основе Jpa должен ")
@DataJpaTest
@Import(BookRepositoryJpa.class)
class BookRepositoryJpaTest {

    private static final int EXPECTED_NUMBER_OF_BOOKS = 2;
    private static final String NEW_BOOK_TITLE = "someNewTitle";

    @Autowired
    private BookRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("находить все записи")
    @Test
    void findAll() {
        List<Book> allBooks = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_BOOKS, allBooks.size());
    }

    @DisplayName("находить запись по ID")
    @Test
    void findById() {
        Optional<Book> optionalAuthor = repositoryJpa.findById(1L);
        assertEquals("title_0", optionalAuthor.get().getTitle());
    }

    @DisplayName("корректно сохранять запись (если она новая)")
    @Test
    void saveWhenNotExists() {
        Author author = new Author();
        em.persist(author);

        Genre genre = new Genre();
        em.persist(genre);

        Book book = new Book();
        book.setTitle(NEW_BOOK_TITLE);
        book.setAuthor(author);
        book.setGenre(genre);

        repositoryJpa.save(book);
        List<Book> allBooks = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_BOOKS + 1, allBooks.size());
    }

    @DisplayName("корректно сохранять запись (если она старая)")
    @Test
    void saveWhenAlreadyExists() {
        Optional<Book> optionalBookBefore = repositoryJpa.findById(2L);
        assertEquals("title_1", optionalBookBefore.get().getTitle());

        Author author = new Author();
        em.persist(author);

        Genre genre = new Genre();
        em.persist(genre);

        Book book = new Book();
        book.setId(2L);
        book.setTitle(NEW_BOOK_TITLE);
        book.setAuthor(author);
        book.setGenre(genre);

        repositoryJpa.save(book);
        em.detach(book);

        List<Book> allBooks = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_BOOKS, allBooks.size());

        Optional<Book> optionalBookAfter = repositoryJpa.findById(2L);
        assertEquals(NEW_BOOK_TITLE, optionalBookAfter.get().getTitle());
    }

    @DisplayName("находить книгу по заголовку")
    @Test
    void findByTitle() {
        List<Book> books = repositoryJpa.findByTitle("title_0");
        assertEquals(1, books.size());
        assertEquals(1L, books.get(0).getId());
        assertEquals("genre_unknown", books.get(0).getGenre().getGenreName());
        assertEquals("author_unknown", books.get(0).getAuthor().getAuthorName());
    }

    @DisplayName("обновлять запись по ID")
    @Test
    void updateTitleById() {
        Book bookBefore = em.find(Book.class, 1L);
        em.detach(bookBefore);

        repositoryJpa.updateTitleById(1, NEW_BOOK_TITLE);

        Book bookAfter = em.find(Book.class, 1L);

        assertThat(bookAfter.getTitle()).isNotEqualTo(bookBefore.getTitle()).isEqualTo(NEW_BOOK_TITLE);
    }

    @DisplayName("удалять запись по ID")
    @Test
    void deleteById() {
        Book firstBook = em.find(Book.class, 2L);
        assertThat(firstBook).isNotNull();
        em.detach(firstBook);

        repositoryJpa.deleteById(2L);
        Book deletedBook = em.find(Book.class, 2L);

        assertThat(deletedBook).isNull();

        List<Book> allBooks = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_BOOKS - 1, allBooks.size());
    }
}