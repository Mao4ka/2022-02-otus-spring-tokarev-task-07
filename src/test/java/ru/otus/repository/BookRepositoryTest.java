package ru.otus.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.entity.Book;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BookRepositoryTest {

    private static final String NEW_TITLE = "SomeNewTitle";
    private static final long FIRST_INDEX = 1;

    @Autowired
    private BookRepository repository;

    @Test
    void updateTitleById() {
        repository.updateTitleById(FIRST_INDEX, NEW_TITLE);

        List<Book> checkedBooks = repository.findByTitle(NEW_TITLE);
        assertNotNull(checkedBooks.get(0));
        assertEquals(NEW_TITLE, checkedBooks.get(0).getTitle());

        assertEquals(NEW_TITLE, repository.findById(FIRST_INDEX).get().getTitle());
    }
}