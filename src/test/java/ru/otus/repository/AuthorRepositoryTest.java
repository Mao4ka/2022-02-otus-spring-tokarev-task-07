package ru.otus.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.entity.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class AuthorRepositoryTest {

    private static final String NEW_AUTHOR_NAME = "Иван Антонович Ефремов";
    private static final long FIRST_INDEX = 1;

    @Autowired
    private AuthorRepository repository;

    @Test
    void updateNameById() {
        repository.updateNameById(FIRST_INDEX, NEW_AUTHOR_NAME);

//        List<Author> allAuthors = repository.findAll();
//        Author author = repository.findById(FIRST_INDEX).get();
//        repository.saveAndFlush(author);

        List<Author> checkedAuthors = repository.findByAuthorName(NEW_AUTHOR_NAME);
        assertNotNull(checkedAuthors.get(0));
        assertEquals(NEW_AUTHOR_NAME, checkedAuthors.get(0).getAuthorName());

        assertEquals(NEW_AUTHOR_NAME, repository.findById(FIRST_INDEX).get().getAuthorName());
    }
}