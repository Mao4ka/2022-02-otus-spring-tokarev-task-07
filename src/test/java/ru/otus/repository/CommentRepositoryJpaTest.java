package ru.otus.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Comment;
import ru.otus.entity.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий Comment на основе Jpa должен ")
@DataJpaTest
@Import(CommentRepositoryJpa.class)
class CommentRepositoryJpaTest {

    private static final int EXPECTED_NUMBER_OF_COMMENTS = 5;
    private static final String NEW_COMMENT_TEXT = "Вы гениальнейше бездарны...";
    private static final String EXPECTED_COMMENT_TEXT = "Аффтар жжот, пешы истчо!";
    private static final String EXPECTED_COMMENT_PART = "жжот";

    @Autowired
    private CommentRepositoryJpa repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("находить все записи")
    @Test
    void findAll() {
        List<Comment> allCommentss = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_COMMENTS, allCommentss.size());
    }

    @DisplayName("находить запись по ID")
    @Test
    void findById() {
        Optional<Comment> optionalComment = repositoryJpa.findById(1L);
        assertEquals(EXPECTED_COMMENT_TEXT, optionalComment.get().getCommentText());
    }

    @DisplayName("корректно сохранять запись (если она новая)")
    @Test
    void saveWhenNotExists() {
        Author author = new Author();
        em.persist(author);

        Genre genre = new Genre();
        em.persist(genre);

        Book book = new Book();
        book.setAuthor(author);
        book.setGenre(genre);
        em.persist(book);

        Comment comment = new Comment();
        comment.setBook(book);
        comment.setCommentText(NEW_COMMENT_TEXT);

        repositoryJpa.save(comment);
        List<Comment> allComments = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_COMMENTS + 1, allComments.size());
    }

    @DisplayName("корректно сохранять запись (если она старая)")
    @Test
    void saveWhenAlreadyExists() {
        Optional<Comment> commentBefore = repositoryJpa.findById(2L);
        String commentTextBefore = commentBefore.get().getCommentText();

        Author author = new Author();
        em.persist(author);

        Genre genre = new Genre();
        em.persist(genre);

        Book book = new Book();
        book.setAuthor(author);
        book.setGenre(genre);
        em.persist(book);

        Comment comment = new Comment();
        comment.setId(2L);
        comment.setBook(book);
        comment.setCommentText(NEW_COMMENT_TEXT);

        repositoryJpa.save(comment);
        em.detach(comment);

        List<Comment> allComments = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_COMMENTS, allComments.size());

        Optional<Comment> commentAfter = repositoryJpa.findById(2L);
        assertThat(commentAfter.get().getCommentText()).isNotEqualTo(commentTextBefore).isEqualTo(NEW_COMMENT_TEXT);
    }

    @Test
    void findByCommentText() {
        List<Comment> comments = repositoryJpa.findByCommentText(EXPECTED_COMMENT_PART);
        assertEquals(1, comments.size());
        assertEquals(1L, comments.get(0).getId());
        assertEquals("title_1", comments.get(0).getBook().getTitle());
    }

    @DisplayName("обновлять запись по ID")
    @Test
    void updateCommentTextById() {
        Comment commentBefore = em.find(Comment.class, 1L);
        em.detach(commentBefore);

        repositoryJpa.updateCommentTextById(1, NEW_COMMENT_TEXT);

        Comment commentAfter = em.find(Comment.class, 1L);

        assertThat(commentAfter.getCommentText()).isNotEqualTo(commentBefore.getCommentText()).isEqualTo(NEW_COMMENT_TEXT);
    }

    @DisplayName("удалять запись по ID")
    @Test
    void deleteById() {
        Comment firstComment = em.find(Comment.class, 2L);
        assertThat(firstComment).isNotNull();
        em.detach(firstComment);

        repositoryJpa.deleteById(2L);
        Comment deletedComment = em.find(Comment.class, 2L);

        assertThat(deletedComment).isNull();

        List<Comment> allComments = repositoryJpa.findAll();
        assertEquals(EXPECTED_NUMBER_OF_COMMENTS - 1, allComments.size());
    }
}