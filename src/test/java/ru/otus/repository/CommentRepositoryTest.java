package ru.otus.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.entity.Book;
import ru.otus.entity.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class CommentRepositoryTest {

    private static final String SEARCHING_COMMENT_TEXT = "Автор";
    private static final String NEW_COMMENT_TEXT = "прекрати это писать!";
    private static final long FIRST_INDEX = 1;

    @Autowired
    private CommentRepository repository;

    @Test
    void findByCommentTextLike() {
        List<Comment> comments = repository.findByCommentTextLike("%" + SEARCHING_COMMENT_TEXT + "%");
        assertEquals(2, comments.size());
    }

    @Test
    void findByBookTitle() {
        List<Comment> comments = repository.findByBookTitleIgnoreCase("TITLE_1");
        assertEquals(4, comments.size());
    }

    @Test
    void findByBookAuthorAuthorName() {
        List<Comment> comments = repository.findByBookAuthorAuthorName("author_01");
        assertEquals(4, comments.size());
    }

    @Test
    void updateNameById() {
        repository.updateCommentTextById(FIRST_INDEX, NEW_COMMENT_TEXT);

        List<Comment> checkedComments = repository.findByCommentTextLike(NEW_COMMENT_TEXT);
        assertNotNull(checkedComments.get(0));
        assertEquals(NEW_COMMENT_TEXT, checkedComments.get(0).getCommentText());

        assertEquals(NEW_COMMENT_TEXT, repository.findById(FIRST_INDEX).get().getCommentText());
    }

    @Test
    void findAllEntityGraph() {
        List<Comment> comments = repository.findAll();
        Comment comment1 = comments.stream()
                .filter(comment -> comment.getId() == 1L)
                .findFirst()
                .get();

        assertEquals("Аффтар жжот, пешы истчо!", comment1.getCommentText());

        //@EntityGraph(attributePaths = "book") работает!!!
        assertEquals("title_1", comment1.getBook().getTitle());
    }


}