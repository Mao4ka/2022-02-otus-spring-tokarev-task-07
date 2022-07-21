package ru.otus.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "book")
    @Override
    List<Comment> findAll();

    List<Comment> findByCommentTextLike (String text);
    List<Comment> findByBookTitleIgnoreCase(String bookTitle);
    List<Comment> findByBookAuthorAuthorName (String bookTitle);

    @Modifying
    @Transactional
    @Query("update Comment c set c.commentText = :text where c.id = :id")
    void updateCommentTextById(@Param("id") long id, @Param("text") String text);

}
